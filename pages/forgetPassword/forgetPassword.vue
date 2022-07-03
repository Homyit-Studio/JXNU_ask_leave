<template>
	<view class="forget-page">
		<uni-forms class="forget-form" :modelValue="formData">
			<uni-forms-item name="email">
				<uni-easyinput type="text" v-model="formData.email" placeholder="请输入已绑定的邮箱" />
			</uni-forms-item>
			<uni-forms-item name="checkcode" class="check-code">
				<uni-easyinput type="text" v-model="formData.checkcode" placeholder="请输入验证码" />
				<button plain="true" size="mini" class="get-button">获取验证码</button>
			</uni-forms-item>
			<uni-forms-item name="newpassword">
				<uni-easyinput type="password" v-model="formData.newpassword" placeholder="请输入新密码" />
			</uni-forms-item>
			<uni-forms-item name="checkpassword">
				<uni-easyinput type="password" v-model="formData.checkpassword" placeholder="请再次确认密码" />
			</uni-forms-item>
			<view class="remind-text">
				<text>密码长度需不少于6位，可含数字、字母和符号</text>
			</view>
		</uni-forms>
		<button class="button" @click="submitForm">确定</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				formData: {
					email: "",
					checkcode: "",
					newpassword: "",
					checkpassword: ""
				}
			}
		},
		methods: {
			submitForm() {}
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
