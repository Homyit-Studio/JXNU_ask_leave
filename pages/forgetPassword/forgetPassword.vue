<template>
	<view class="forget-page">
		<uni-notice-bar text="点击确认后新密码将发到你绑定的邮箱,登录成功后请尽快修改你的密码!"
			showIcon></uni-notice-bar>
		<uni-easyinput type="text" v-model="studentNumber" placeholder="请输入你的学号或工号" />
		<button class="button" @click="submitForm">确定</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				studentNumber: ""
			}
		},
		methods: {
			submitForm() {
				uni.$http.get(`/user/resetPasswordByEmail/${this.studentNumber}`).then(res => {
					if(res.data.code == 200){
						uni.showToast({
							title:"修改成功"
						})
						setTimeout(() => {
							uni.navigateBack({
								animationDuration:100
							})
						},1000)
					}else{
						uni.showToast({
							title:res.data.message,
							icon:"error"
						})
					}
				}).catch(err => {
					uni.showToast({
						title:"请检查输入是否正确",
						icon:"error"
					})
				})
			}
		}
	}
</script>

<style lang="scss">
	.forget-page {
		width: 700rpx;
		margin: 0 auto;
		margin-top: 10px;
		.forget-form{
			// width: 700rpx;
			// margin: 0 auto;
			.check-code{
				position: relative;
				.get-button{
					position: absolute;
					top: 5px;
					right: 25px;
					height: 25px;
					line-height: 25px;
					font-size: 12px;
				}
			}
			.remind-text{
				color: red;
				font-size: 12px;
				padding-left: 10px;
			}
		}
		.button {
			margin-top: 50rpx;
			background-color: $jxnu-bg-color;
			color: $uni-text-color-inverse;
			border-radius: 100rpx;
			font-size: $jxnu-font-14;
		}
	}
</style>
