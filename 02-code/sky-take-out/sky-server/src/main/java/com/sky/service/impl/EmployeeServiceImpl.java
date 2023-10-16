package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static com.sky.constant.StatusConstant.ENABLE;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传过来的明文密码进行加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
     * 新增员工
     * @param employeeDTO
     */
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("save-service--->当前线程的id:" + Thread.currentThread().getId());
        //将dto里面的属性存放到entity实体类中
        Employee employee = new Employee();
        //方法1：一个一个复制，很麻烦
        //employee.setName(employeeDTO.getName());
        //employee.setPhone(employeeDTO.getPhone());
        //...

        //方法2：对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        //把左边对象的属性【注入到】右边的对象
        //一些Employee对象特有的属性，手动赋值
        //1.设置账户状态 1:正常 0：锁定
        employee.setStatus(ENABLE);
        //2.设置密码，默认密码：123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes(StandardCharsets.UTF_8)));
        //3.设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //后期需要改为当前登录用户的id——要动态获取
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //select * from employee limit 0,10
        //开始分页查询
        //使用插件，这个插件底层使用mybatis的拦截器进行实现
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        //map返回的是page对象，我们要对page对象进行加工处理，变成PageResult对象
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        return new PageResult(total,records);
    }

}
