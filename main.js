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

$http.baseUrl = 'http://101.43.85.67:8081'

$http.beforeRequest = function(options) {
	if(options.url != "http://101.43.85.67:8081/user/login"){
		options.header["token"] = uni.getStorageSync('token') 
	}
	uni.showLoading({
		title: '正在加载中...',
	})
}

$http.afterRequest = function() {
	uni.hideLoading()
}
