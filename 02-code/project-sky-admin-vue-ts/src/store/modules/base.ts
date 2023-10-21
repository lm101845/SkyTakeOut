/**
 * @Author liming
 * @Date 2023/10/21 16:23
 **/
//TODO:以后再优化这个吧

// export default new Vuex.Store({
//   state: {
//     // 用于页面直接传递数据
//     pageCache
//   },
//
//   getters: {
//     // 页面取数据
//     getPageCache: (state) => (key = null) => {
//       return key ? state.pageCache[key] : state.pageCache;
//     }
//   },
//
//   mutations: {
//     SET_PAGE_CACHE(state, {key, value}) {
//       state.pageCache[key] = value;
//       storage.setJSON('pageCache', state.pageCache);
//     },
//     CLEAR_PAGE_CACHE(state) {
//       state.pageCache = {};
//       storage.setJSON('pageCache', state.pageCache);
//     }
//   },
//
//   actions: {
//     // 页面设置数据 {key, value: {}}
//     setPageCache({commit}, res) {
//       commit('SET_PAGE_CACHE', res);
//     },
//     clearPageCache({commit}) {
//       commit('CLEAR_PAGE_CACHE');
//     }
//   },
// });


//===========================================================
// import { VuexModule, Module, Mutation, Action, getModule } from 'vuex-module-decorators'
// import { getSidebarStatus, setSidebarStatus } from '@/utils/cookies'
// import store from '@/store'
//
// export enum DeviceType {
//   Mobile,
//   Desktop
// }
//
// export interface IAppState {
//   device: DeviceType
//   sidebar: {
//     opened: boolean
//     withoutAnimation: boolean
//
//   }
//   statusNumber:Number
// }
//
// @Module({ 'dynamic': true, store, 'name': 'app' })
// class App extends VuexModule implements IAppState {
//   public sidebar = {
//     'opened': true, //getSidebarStatus() !== 'closed',
//     'withoutAnimation': false
//   }
//   public device = DeviceType.Desktop
//   public statusNumber = 0
//   @Mutation
//   private TOGGLE_SIDEBAR(withoutAnimation: boolean) {
//     this.sidebar.opened = !this.sidebar.opened
//     this.sidebar.withoutAnimation = withoutAnimation
//     if (this.sidebar.opened) {
//       setSidebarStatus('opened')
//     } else {
//       setSidebarStatus('closed')
//     }
//   }
//
//   @Mutation
//   private CLOSE_SIDEBAR(withoutAnimation: boolean) {
//     this.sidebar.opened = false
//     this.sidebar.withoutAnimation = withoutAnimation
//     setSidebarStatus('closed')
//   }
//
//   @Mutation
//   private STATUS_NUMBER(device: DeviceType) {
//     this.statusNumber = device
//   }
//
//   @Mutation
//   private TOGGLE_DEVICE(device: DeviceType) {
//     this.device = device
//   }
//
//   @Action
//   public ToggleSideBar(withoutAnimation: boolean) {
//     this.TOGGLE_SIDEBAR(withoutAnimation)
//   }
//
//   @Action
//   public CloseSideBar(withoutAnimation: boolean) {
//     this.CLOSE_SIDEBAR(withoutAnimation)
//   }
//
//   @Action
//   public ToggleDevice(device: DeviceType) {
//     this.TOGGLE_DEVICE(device)
//   }
//
//   @Action
//   public StatusNumber(device: any) {
//     this.STATUS_NUMBER(device)
//   }
// }
//
// export const AppModule = getModule(App)

