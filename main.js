import App from './App'
import 'default-passive-events'//谷歌浏览器

// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
// #endif

// 按需导入 $http 对象
import {
	$http
} from '@escook/request-miniprogram'
uni.$http = $http

$http.baseUrl = 'https://blog.aday.top/api'

$http.beforeRequest = function(options) {
	if (options.url != "https://blog.aday.top/api/user/login") {
		options.header["token"] = uni.getStorageSync('token')
	}
	// uni.showLoading({
	// 	title: '正在请求中'
	// });
}

$http.afterRequest = function(res) {
	// uni.hideLoading()
	if(res.data.code == 567){
		uni.showToast({
			title:"登录失效，请重新登录",
			icon:"exception"
		})
		setTimeout(() => {
			uni.redirectTo({
				url:"/pages/index/index"
			})
		}, 1000)
	}
}
