<template>
	<view class="login-content">
		<view>
			<image class="login-img" src="https://uis.jxnu.edu.cn/cas/file/png/logoM.png" lazy-load mode="aspectFit"
				fade-show></image>
		</view>
		<view class="login-card">
			<text class="uni-h6">账号登录</text>
		</view>
		<view class="login-form" v-if="optionChoose">
			<uni-forms ref="baseForm" :modelValue="loginFormData">
				<uni-forms-item>
					<uni-easyinput prefixIcon=".uniui-person-filled" v-model="loginFormData.studentNumber" placeholder="请输入工号或学号" />
				</uni-forms-item>
				<uni-forms-item>
					<uni-easyinput prefixIcon=".uniui-locked-filled" v-model="loginFormData.password" placeholder="请输入登录密码" />
				</uni-forms-item>
			</uni-forms>
			<button type="default" class="button" @click="loginSubmit">提交</button>
		</view>
		<view class="login-form" v-else>
			<uni-forms ref="baseForm" :modelValue="loginFormData">
				<uni-forms-item>
					<uni-easyinput prefixIcon=".uniui-person-filled" v-model="loginFormData.teacherNumber" placeholder="请输入工号或学号" />
				</uni-forms-item>
				<uni-forms-item>
					<uni-easyinput prefixIcon=".uniui-locked-filled" v-model="loginFormData.password" placeholder="请输入登录密码" />
				</uni-forms-item>
			</uni-forms>
			<button type="default" class="button" @click="loginSubmit">提交</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loginFormData:{
				},
				optionChoose: ""
			}
		},
		onLoad(option) {
			this.optionChoose = option.card == "student" ? true : false
			this.cardChoose(option.card)
		},
		computed:{
		},
		methods: {
			cardChoose(card){
				if(card == "student"){
					this.loginFormData = {
						"studentNumber":null,
						"password":""
					}
				}else{
					this.loginFormData = {
						"teacherNumber":null,
						"password":""
					}
				}
			},
			loginSubmit(){
				console.log(this.loginFormData)
			}
		}
	}
</script>

<style lang="scss">
	.login-content {
		width: 700rpx;
		display: flex;
		justify-content: center;
		flex-wrap: wrap;
		margin: 0 auto;

		.login-img {
			width: 500rpx;
			height: 200rpx;
		}
		.login-card{
			color: $jxnu-bg-color;
			text-align: center;
			width: 700rpx;
			margin-bottom: 40rpx;
		}
	}
	
	.login-form{
		.uni-easyinput{
			width: 650rpx;
			border-radius: 100rpx;
		}
		.button{
			margin-top: 50rpx;
			background-color: $jxnu-bg-color;
			color: $uni-text-color-inverse;
			border-radius: 100rpx;
		}
	}
</style>
