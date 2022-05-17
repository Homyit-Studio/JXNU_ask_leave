<template>
	<view class="teacher-home">
		<view class="backgrond-style">
		</view>
		<view class="teacher-message">
			<uni-card :title="studentMessage.username" :extra="studentMessage.studentNumber + ''" sub-title="学生">
				<view slot="actions" class="card-actions">
					<view class="card-actions-item" @click="revisePassword">
						<uni-icons type="loop" size="20" color="#999"></uni-icons>
						<text class="card-actions-item-text">修改密码</text>
					</view>
				</view>
			</uni-card>
		</view>
		<view class="teacher-tools">
			<uni-card>
				<view>
					<text>常用工具</text>
				</view>
				<view class="tools-box">
					<view>
						<navigator animation-type="pop-in" animation-duration="300" url="/pages/applyLeave/applyLeave" class="tools-btn">
							<uni-icons type="mail-open" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</navigator>
						<text>请假申请</text>
					</view>
					<view>
						<navigator animation-type="pop-in" animation-duration="300" url="/pages/allLeaves/allLeaves" class="tools-btn">
							<uni-icons type="personadd" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</navigator>
						<text>查看申请</text>
					</view>
					<view>
						<view class="tools-btn" @click="logOut()">
							<uni-icons type="paperplane" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</view>
						<text>退出登录</text>
					</view>
				</view>
			</uni-card>
		</view>
		<view class="teacher-tools">
			<uni-card>
				<view>
					<text>离校报备</text>
				</view>
				<view class="tools-box">
					<view>
						<view class="tools-btn" @click="goTerminate()">
							<uni-icons type="mail-open" size="35" color="#f0f0f0" class="icon-style"></uni-icons>
						</view>
						<text>假条销假</text>
					</view>
				</view>
			</uni-card>
		</view>
		<!-- 确认退出弹框 -->
		<uni-popup ref="dialog_up" type="dialog">
			<uni-popup-dialog type="error" content="确认退出？" :duration="2000" :before-close="true" @close="close" @confirm="confirm"></uni-popup-dialog>
		</uni-popup>
		<!-- 修改密码弹窗 -->
		<uni-popup ref="popupRevisePassword" background-color="#fff" type="bottom">
			<view class="revise-popup-content">
				<uni-forms class="revise-form" ref="revisePasswordForm" :modelValue="reviseFormData" :rules="dataRules" validateTrigger="submit">
					<uni-forms-item name="oldPassword">
						<uni-easyinput type="password" prefixIcon=".uniui-person-filled"
							v-model="reviseFormData.oldPassword" placeholder="请输入原密码" />
					</uni-forms-item>
					<uni-forms-item name="newPassword">
						<uni-easyinput type="password" prefixIcon=".uniui-locked-filled"
							v-model="reviseFormData.newPassword" placeholder="请设置新密码" />
					</uni-forms-item>
				</uni-forms>
				<view class="revise-buttons">
					<button type="default" class="cancel-button" @click="cancelSubmit">取消</button>
					<button type="default" class="revise-button" @click="reviseSubmit">确认</button>
				</view>
			</view>
		</uni-popup>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				studentMessage: {
					studentNumber: "null",
					username: "null"
				},
				reviseFormData: {
					oldPassword: "",
					newPassword: ""
				},
				dataRules:{
					"oldPassword":{
						rules:[
							{
								required: true,
								errorMessage: "请输入原密码"
							}]
					},
					"newPassword":{
						rules:[
							{
								required: true,
								errorMessage: "请输入新密码"
							}]
					},
				}
			}
		},
		onLoad() {
			this.getData();
		},
		methods: {
			getData(){
				uni.$http.get('/user/personInfo').then((res)=>{
					console.log(res.data.data)
					let userData = res.data.data;
					this.studentMessage = userData;
					
					//储存学生信息，方便其他页面获取
					uni.setStorage({
						key: 'userStr',
						data: JSON.stringify(userData),
						success: function () {
							//console.log('success');
						}
					});
				}).catch((err)=>{
					console.log(err)
				})
			},
			revisePassword() {
				this.$refs.popupRevisePassword.open()
			},
 
			//提交修改密码
			reviseSubmit() {
				this.$refs.revisePasswordForm.validate().then(res=>{
					uni.showToast({
						title: "正在修改中"
					})
					uni.$http.post('/user/updatePWD', {
						  "oldPassword": this.reviseFormData.oldPassword,
						  "newPassword": this.reviseFormData.newPassword
					}).then(res=>{
						console.log(res)
						if(res.data.code === 200){
							uni.showToast({
								title: "修改成功,请重新登录",
							})
							setTimeout(()=>{
								this.$refs.popupRevisePassword.close();
								this.confirm();//相当于退出登录
							},1500)
						}else{
							this.$errShowToast(res.data.message);
						}
					})
				}).catch(err =>{
					this.$errShowToast(err);
				})
			},
			//退出登录
			logOut(){
				this.$refs.dialog_up.open()
			},
			//弹框操作
			close() {
				this.$refs.dialog_up.close()
			},
			//确认退出
			confirm() {
				uni.$http.get('/user/logout').then(res =>{
					console.log(res)
					if(res.data.code === 200){
						uni.clearStorage();//清理本地缓存
						uni.redirectTo({
							url: '/pages/index/index'
						})	
					}
					this.$refs.dialog_up.close();
				}).catch(err=>{
					console.log(err)
				})
			},
			//销假路上
			goTerminate(){
				uni.showToast({
					title:'请在 查看详情->我要销假 页面进行销假操作',
					icon:'none',
				}
				);
				setTimeout(()=>{
					uni.navigateTo({
						url: '/pages/allLeaves/allLeaves'
					})
				},2000)	
			}
			
		}
	}
</script>

<style lang="scss">
	@mixin bgstyle($bgstyle: #1b478e) {
		width: 100vw;
		height: 310rpx;
		position: absolute;
		top: -100rpx;
		border-radius: 0 0 40rpx 40rpx;
		background: $bgstyle;
	}
	
	.backgrond-style {
		@include bgstyle;
	}
	
	.teacher-home {
		.card-actions-item {
			display: flex;
			justify-content: flex-end;
		}
	}
	
	.revise-form {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 50rpx;
	
		.uni-easyinput {
			width: 650rpx;
			border-radius: 100rpx;
		}
	}
	
	.revise-popup-content {
		.revise-buttons {
			display: flex;
			justify-content: center;
			padding-bottom: 50rpx;
	
			button {
				width: 300rpx;
				height: 60rpx;
				line-height: 60rpx;
				font-size: $jxnu-font-14;
			}
	
			.revise-button {
				background-color: $jxnu-bg-color;
			}
		}
	}
	
	.teacher-tools {
		.tools-box {
			display: flex;
			flex-direction: row;
			justify-content: flex-start;
			text-align: center;
	
			.tools-btn {
				padding: 20rpx;
				margin: 20rpx;
				background-color: $jxnu-bg-color;
				color: $uni-text-color;
				border-radius: 30rpx;
			}
		}
	}
</style>
