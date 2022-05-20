<template>
	<view class="class-roster-page">
		<uni-card v-for="(item, i) in teacherList" :key="item.id">
			<view class="message-card" @click="showStudent(item)">
				<view class="student-message">
					<image
						:src="item.sex  == 'WOMAN'? 'https://img.tukuppt.com/png_preview/00/05/38/DCoSdpcUVs.jpg!/fw/780' : 'https://img.tukuppt.com/png_preview/00/05/28/LjhCsodQxt.jpg!/fw/780'">
					</image><text>{{item.username}}</text>
				</view>
				<view><text>工号:{{"00" + item.studentNumber}}</text></view>
			</view>
		</uni-card>
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="studentPopup" background-color="#fff" type="bottom">
				<view class="popup-content">
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
			</uni-popup>
		</view>
		<view v-if="shownodata">
			<view class="show-nodata"><text>没有更多数据了</text></view>
		</view>
		<!-- 修改信息弹框 -->
		<uni-popup ref="reviseDialog" type="dialog">
			<template v-slot:default>
				<view class="confirm-dialog">
					<uni-forms :modelValue="reviseMessage" label-position="left" label-width="60">
							<uni-forms-item label="新名字" name="username" required>
								<uni-easyinput v-model="reviseMessage.username"
																placeholder="请输入修改成的名字" />
							</uni-forms-item>

					</uni-forms>
					<view class="confirm-button">
						<button plain size="mini" @click="cancelConfirm">取消</button>
						<button plain size="mini" @click="dialogInputConfirm">确认</button>
					</view>
				</view>
			</template>
		</uni-popup>
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
				reviseMessage :{
					username:""
				}
			}
		},
		onLoad(options) {
			this.enterChoose = options.choose;
			this.requestteacherRoster()
		},
		methods: {
			requestteacherRoster() {
				let status = ""
				if(this.enterChoose == '0'){
					status = "INSTRUCTOR"
				}else if(this.enterChoose == '1'){
					status = 'SECRETARY'
				}
				uni.$http.get(`/user/getUserByRole?role=${status}`).then(res => {
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
					url:`../addMember/addMember?people=0&id=${this.classId}`
				})
			},
			//删除学生
			deleteStudent() {
				uni.$http.post("/user/deletedUser", {
					id: this.teacherMessage.id,
					studentNumber: this.teacherMessage.studentNumber
				}).then(res => {
					if (res.data.code == 200) {
						console.log(res)
						this.msg.msgType = "success"
						this.msg.messageText = res.data.message
						this.rosterRequest.pageNo = 1
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
			},
			//修改信息
			modifyStudent(){
				this.$refs.reviseDialog.open()
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

		.show-nodata {
			text-align: center;
			padding: 20px;
		}
	}
</style>
