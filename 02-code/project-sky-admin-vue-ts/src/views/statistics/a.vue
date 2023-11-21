<!--
 * @Author liming
 * @Date 2023/11/20 16:13
-->
<template>
  <div class='item-box'>
    <div class='item'>
      <div class='item-b'>
        <span class='item-nums'>
          你好<span>次</span>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'a'
}
</script>

<style scoped lang='less'>
.item-box{
  display: flex;
  flex-direction: column;
  .item{
    display: flex;
    justify-content: space-between;
    .item-b{
      display: flex;
      align-items: center;
      width: 93%;
      .item-nums{
        flex: 0 0 auto;
      }
    }
  }
}
</style>
