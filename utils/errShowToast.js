//请求发生错误时的处理函数

const errShowToast = function(err){
		let msg;
		msg = err.length > 7 ? '请求错误,请重试' : err;
		uni.showToast({
			title: msg,//只能显示7个汉字长度
			icon :'error'
		})
		//this.$router.go(0)//强制刷新当前页面
	}

export default{
	errShowToast
}

