<template>
	<view class="student-home">
		<view class="backgrond-style"></view>
		<view>
			<uni-card class="uni-card">
			    <view class="welcome-box" @click="showStudentMsg()">
					<text>欢迎，{{studentMsg.id}}</text>
					<uni-icons type="forward" size="20" color="#585858" class="more-icon" ></uni-icons>
			    </view>
			</uni-card>
		</view>
		<view>
			<uni-card class="uni-card">
				<uni-row>
					   <text>常用工具</text>
				</uni-row>
			    <uni-row class="btn-box">
			    		<view class="btn-style">
							<navigator url="/pages/applyLeave/applyLeave" animation-type="pop-in" animation-duration="300">
								<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style" ></uni-icons></view>
							</navigator>
							<text>请假申请</text>
						</view>
						<view class="btn-style">
							<navigator url="/pages/allLeaves/allLeaves" animation-type="pop-in" animation-duration="300">
								<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style"></uni-icons></view>
							</navigator>
							<text>申请记录</text>
						</view>
						<view class="btn-style">
							<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style"></uni-icons></view>
							<text>退出登录</text>
						</view>
			    </uni-row>
			</uni-card>
			<uni-card class="uni-card">
				<uni-row>
					   <text>离校记录</text>
				</uni-row>
			    <uni-row class="btn-box">
			    		<view class="btn-style">
							<navigator url="/pages/confirmLeftSchool/confirmLeftSchool" animation-type="pop-in" animation-duration="300">
								<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style" ></uni-icons></view>
							</navigator>
							<text>确认离校</text>
						</view>
						<view class="btn-style">
							<navigator url="/pages/confirmBackSchool/confirmBackSchool" animation-type="pop-in" animation-duration="300">
								<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style"></uni-icons></view>
							</navigator>
							<text>确认返校</text>
						</view>
			    </uni-row>
			</uni-card>
		</view>
		<!-- 学生信息弹框 -->
		<uni-popup ref="popup" type="bottom">
			<uni-list class="list-style">
				<uni-list-item  title="学号" :rightText="studentMsg.id" ></uni-list-item>
				<uni-list-item  title="姓名" :rightText="studentMsg.name" ></uni-list-item>
				<uni-list-item  title="班级" :rightText="studentMsg.class"></uni-list-item>
				<uni-list-item  title="手机号" :rightText="studentMsg.phoneNumber"></uni-list-item>
				<uni-list-item  title="辅导员" :rightText="studentMsg.instructor"></uni-list-item>
				<view slot="actions" class="card-actions">
					<view class="card-actions-item">
						<button type="default" @click="changePassword()">
							修改密码
						</button>
					</view>
				</view>
			</uni-list>
		</uni-popup>
		<!-- 密码修改弹框 -->
		<uni-popup ref="pass_word_up" type="dialog">
			<view class="password-dialog">
				<view class="password-dialog-title">
					<text>修改密码</text>
				</view>
				<view>
					<uni-easyinput  type="password" prefixIcon=".uniui-person-filled" v-model="studentMsg.changePassWord.PassWord1"  placeholder="请输入新密码" />
				</view>
				<view>
					<uni-easyinput  type="password" prefixIcon=".uniui-person-filled" v-model="studentMsg.changePassWord.PassWord2"  placeholder="请再次输入密码" />
				</view>
				<view class="btn-boder">
					<view class="btn-grounps">
						<text @click="close()">取消</text>
						<text class="right" @click="confirm()">确认</text>
					</view>
				</view>
				</view>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				studentMsg:{
					id:"202026202156",
					name:'薛二狗',
					class:'26级计算机科学与技术5班',
					instructor:'樊砂砾',
					phoneNumber:'13678058666',
					changePassWord:{
						PassWord1:'',
						PassWord2:'',
					}
				},
			}
		},
		methods:{
			showStudentMsg(){
				//弹出学生信息页面
				this.$refs.popup.open()
			},
			changePassword(){
				this.$refs.pass_word_up.open()
			},
			close() {
				this.$refs.pass_word_up.close()
			},
			confirm(value) {
				this.$refs.pass_word_up.close()
			}
	},
	}
</script>

<style lang="scss">
	$dialog_w : 600rpx;
	@mixin bgstyle($bgstyle: #1b478e) {
		width: 100vw;
		height: 310rpx;
		position: absolute;
		top: -100rpx;
		border-radius: 0 0 40rpx 40rpx;
		background: $bgstyle
	}
	.backgrond-style {
		@include bgstyle;
	}
	
	.student-home{
		margin: 0 auto;
		.uni-card{
			border-radius: 20rpx;
			text{
				font-size: $jxnu-font-14;
			}
			.welcome-box{
				height: 200rpx;
				text{
					line-height: 200rpx;
					font-size: $jxnu-font-18;
				}
			}
			.btn-box{
				height: 180rpx;
				margin-top: 20rpx;
			}
			.btn-style{
				display: inline-block;
				margin-right: 50rpx;
				width: 100rpx;
				height: 100rpx;
				background-color: $jxnu-bg-color;
				border-radius: 30rpx;
				text-align: center;
				color: $uni-text-color;
				text{
					font-size: 13rpx;
				}
			}
			.icon-style{
				line-height: 100rpx;
			}
			.more-icon{
				float: right;
			}
		}
		.list-style{
			height: 50vh;
			margin: 0 auto;
			uni-view{
				height: 100rpx;
			}
			.card-actions-item{
				display: flex;
				justify-content: flex-end;
				margin-top: 20rpx;
				margin-right: 20rpx;
				button{
					margin-top: 20rpx;
					width: 600rpx;
					height: 80rpx;
					background-color: $jxnu-bg-color;
					color: $uni-bg-color;
					font-size: $jxnu-font-16;
				}
			}
		}
		
	}	
	.password-dialog{
		width: $dialog_w;
		height: 400rpx;
		background-color:$uni-bg-color ;
		border: 1px #ffffff solid;
		border-radius: 20rpx;
		.password-dialog-title{
			text-align: center;
			color: $uni-text-color-placeholder;
			font-size: $jxnu-font-16;
			margin-top: 20rpx;
			// margin-bottom: 20rpx;
		}
		view{
			width: 480rpx;
			margin: 0 auto;
			margin-top: 30rpx;
			margin-bottom: 10rpx;
			display: block;
		}
		.btn-boder{
			width: $dialog_w;
			border-top: 3px $uni-bg-color-grey solid;
			.btn-grounps{
				width:350rpx;
				margin-top: 30rpx;
				font-size: $jxnu-font-16;
				.right{
					float: right;
					color: $jxnu-bg-color;
				}
			}
		}
	}
	
</style>
