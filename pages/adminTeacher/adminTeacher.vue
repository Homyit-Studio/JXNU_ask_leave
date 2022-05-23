<template>
	<view class="class-roster-page">
		<uni-card v-for="(item, i) in teacherList" :key="item.id">
			<view class="message-card" @click="showStudent(item)">
				<view class="student-message">
					<image
						:src="item.sex  == 'WOMAN'? 'https://img.51miz.com/preview/element/00/01/12/71/E-1127122-0E7A71E8.jpg' : 'https://img.51miz.com/preview/element/00/01/12/75/E-1127590-2FA28D01.jpg'">
					</image><text>{{item.username}}</text>
				</view>
				<view><text>工号:{{"00" + item.studentNumber}}</text></view>
			</view>
		</uni-card>
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="studentPopup" background-color="#fff" type="bottom" @maskClick="cancelmark">
				<view class="popup-content" v-if="modify == 0">
					<view class="remind-tol"><text>点击灰色遮罩层，隐藏教师信息</text></view>
					<uni-list>
						<uni-list-item title="姓名" :rightText="teacherMessage.username"></uni-list-item>
						<uni-list-item title="工号" :rightText="'00' + teacherMessage.studentNumber"></uni-list-item>
						<uni-list-item title="性别" :rightText="teacherMessage.sex  == 'WOMAN'? '女' : '男'">
						</uni-list-item>
						<uni-list-item title="联系方式" :rightText="teacherMessage.phoneNumber"></uni-list-item>
					</uni-list>
					<view class="revise-buttons">
						<button type="default" class="cancel-button" @click="modifyStudent">修改信息</button>
						<button type="warn" class="revise-button" @click="deleteStudent">删除老师</button>
					</view>
				</view>
				<view v-else class="modify-member">
					<view class="remind-tol"><text>点击灰色遮罩层，取消修改</text></view>
					<uni-forms ref="form" :modelValue="peopleformData" class="form-style" :border="true"
						validateTrigger="bind" err-show-type="toast">
						<uni-forms-item required label="姓名" name="username">
							<uni-easyinput multiple v-model="peopleformData.username" placeholder="请输入教师的姓名"
								:inputBorder="false" />
						</uni-forms-item>
						<uni-forms-item required label="学号" name="studentnumber">
							<uni-easyinput multiple v-model="peopleformData.studentNumber" placeholder="请输入工号"
								:inputBorder="false" />
						</uni-forms-item>
						<uni-forms-item required label="电话" name="phonenumber">
							<uni-easyinput multiple v-model="peopleformData.phoneNumber" placeholder="请输入教师联系方式"
								:inputBorder="false" />
						</uni-forms-item>
						<button @click="submitForm" class="confirm-button">确认</button>
					</uni-forms>
				</view>
			</uni-popup>
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
		<!-- 添加成员 -->
		<view>
			<uni-fab ref="fab" :content="content" horizontal="right" vertical="bottom" :direction="direction"
				@trigger="triggerAdd" />
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				content: [{
					iconPath: '/static/add1.png',
					selectedIconPath: '/static/add.png',
					text: '添加成员',
					active: false
				}],
				enterChoose: null,
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				options: [{
						text: '增加',
						style: {
							backgroundColor: '#007aff',
							fontSize: "12px"
						}
					},
					{
						text: '更换',
						style: {
							backgroundColor: '#09dd57',
							fontSize: "12px"
						}
					},
					{
						text: '删除',
						style: {
							backgroundColor: '#dd524d',
							fontSize: "12px"
						}
					}
				],
				teacherList: [],
				teacherMessage: {},
				reviseMessage: {
					username: ""
				},
				modify: 0,
				peopleformData: {}
			}
		},
		onLoad(options) {
			this.enterChoose = options.choose;
			this.requestteacherRoster()
		},
		methods: {
			requestteacherRoster() {
				uni.$http.get(`/user/getUserByRole?role=${this.enterChoose}`).then(res => {
					if (res.data.code == 200) {
						this.teacherList = res.data.data
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
						this.shownodata = true
					}
				})
			},
			showStudent(item) {
				this.teacherMessage = item
				this.$refs.studentPopup.open()
			},
			//添加成员
			triggerAdd(e) {
				uni.navigateTo({
					url: `../addMember/addMember?people=${this.enterChoose}`
				})
			},
			//修改信息
			modifyStudent() {
				this.modify = 1
				this.peopleformData = JSON.parse(JSON.stringify(this.teacherMessage))
			},
			cancelmark() {
				this.modify = 0
			},
			submitForm() {
				uni.$http.post("/user/updateUser",
					this.peopleformData).then(res => {
					if (res.data.code == 200) {
						console.log(res)
						this.msg.msgType = "success"
						this.msg.messageText = "修改成功"
						this.modify = 0
						setTimeout(()=>{
							uni.showToast({
								title:"请求成功",
								icon:"success"
							})
						},600)
						this.$refs.message.open()
						this.$refs.studentPopup.close()
						this.requestteacherRoster()
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "修改信息错误"
						this.$refs.message.open()
						this.$refs.studentPopup.close()
					}
				})
			},
			//删除老师
			deleteStudent() {
				uni.$http.post("/user/deletedUser", {
					id: this.teacherMessage.id,
					studentNumber: this.teacherMessage.studentNumber
				}).then(res => {
					if (res.data.code == 200) {
						console.log(res)
						this.msg.msgType = "success"
						this.msg.messageText = res.data.message
						setTimeout(()=>{
							uni.showToast({
								title:"请求成功",
								icon:"success"
							})
						},600)
						this.$refs.message.open()
						this.$refs.studentPopup.close()
						this.requestClassRoster(this.classId)
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
						this.$refs.studentPopup.close()
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	.class-roster-page {
		.remind {
			text-align: center;
		}

		.confirm-dialog {
			width: 570rpx;
			padding: 10px 15px;
			border-radius: 10px;
			background-color: #fff;

			textarea {
				border: 1px solid #e2e2e2;
				width: 550rpx;
				border-radius: 5px;
				padding: 5px;
				margin-top: 5px;
				height: 150rpx;
			}

			.text {
				margin-bottom: 5px;
			}

			.confirm-button {
				display: flex;
				justify-content: space-around;
				padding-top: 10rpx;

				button {
					width: 250rpx;
					height: 60rpx;
					border: none;
					line-height: 60rpx;
					font-size: $jxnu-font-14;
				}
			}
		}

		.revise-buttons {
			display: flex;
			justify-content: center;
			padding-bottom: 50rpx;
			margin-top: 10px;

			button {
				width: 300rpx;
				height: 60rpx;
				line-height: 60rpx;
				font-size: $jxnu-font-14;
			}
		}

		.remind-tol {
			text-align: center;
			font-size: 12px;
			margin: 5rpx;
		}

		.student-title {
			text-align: center;
			padding: 10rpx 0;
		}

		.student-message {
			display: flex;
			align-items: center;

			image {
				width: 100rpx;
				height: 100rpx;
				border-radius: 50%;
			}
		}

		.message-card {
			display: flex;
			flex-wrap: nowrap;
			justify-content: space-between;
			align-items: center;
		}
		.modify-member {
					width: 90vw;
					margin: 0 auto;
		
					.confirm-button {
						width: 400rpx;
						height: 30px;
						line-height: 30px;
						margin-bottom: 10px;
						background-color: $jxnu-bg-color;
						color: #fff;
					}
				}

		.show-nodata {
			text-align: center;
			padding: 20px;
		}
	}
</style>
