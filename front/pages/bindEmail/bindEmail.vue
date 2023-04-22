<template>
	<view class="bindemail-page">
		<uni-forms class="forget-form" :modelValue="formData">
			<uni-forms-item name="email">
				<uni-easyinput type="text" v-model="formData.email" placeholder="请输入已绑定的邮箱" />
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
				}
			}
		},
		methods: {
			submitForm() {}
		}
	}
</script>

<style lang="scss">
	.bindemail-page {
		width: 700rpx;
		margin: 0 auto;
		margin-top: 10px;
		.forget-form{
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
