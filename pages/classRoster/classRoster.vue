<template>
	<view class="class-roster-page">
		<view class="student-title"><text>{{className}}</text></view>
		<uni-card v-for="(item, i) in studentList" :key="item.classNumber">
			<view class="message-card" @click="showStudent(item)">
				<view class="student-message">
					<image
						:src="item.sex  == 'WOMAN'? 'https://img.tukuppt.com/png_preview/00/05/38/DCoSdpcUVs.jpg!/fw/780' : 'https://img.tukuppt.com/png_preview/00/05/28/LjhCsodQxt.jpg!/fw/780'">
					</image><text>{{item.username}}</text>
				</view>
				<view><text>学号:{{item.studentNumber}}</text></view>
			</view>
		</uni-card>
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="studentPopup" background-color="#fff" type="bottom">
				<view class="popup-content">
					<view class="remind-tol"><text>点击灰色遮罩层，隐藏学生信息</text></view>
					<uni-list>
						<uni-list-item title="姓名" :rightText="studentMessage.username"></uni-list-item>
						<uni-list-item title="学号" :rightText="'' + studentMessage.studentNumber"></uni-list-item>
						<uni-list-item title="班级" :rightText="studentMessage.majorAndClass"></uni-list-item>
						<uni-list-item title="宿舍" :rightText="studentMessage.dormitoryNumber"></uni-list-item>
						<uni-list-item title="性别" :rightText="studentMessage.sex  == 'WOMAN'? '女' : '男'">
						</uni-list-item>
						<uni-list-item title="民族" :rightText="studentMessage.nation"></uni-list-item>
						<uni-list-item title="出生地" :rightText="studentMessage.nativePlace"></uni-list-item>
						<uni-list-item title="联系方式" :rightText="studentMessage.phoneNumber"></uni-list-item>
						<uni-list-item title="父母电话" :rightText="studentMessage.parentNumber"></uni-list-item>
						<uni-list-item title="家庭地址" :note="studentMessage.homeAddress"></uni-list-item>
					</uni-list>
					<view class="revise-buttons" v-if="enterChoose == '0'">
						<button type="default" class="cancel-button" @click="modifyStudent">修改信息</button>
						<button type="warn" class="revise-button" @click="deleteStudent">删除学生</button>
					</view>
				</view>
			</uni-popup>
		</view>
		<view v-if="shownodata">
			<view class="show-nodata"><text>没有更多数据了</text></view>
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
		<!-- 添加成员 -->
		<view v-if="enterChoose == '0'">
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
				//没有更多数据提醒
				shownodata: false,
				enterChoose: null,
				//节流阀
				isloading: false,
				className: null,
				//数据总数
				endPage: null,
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
				studentList: [],
				studentMessage: {},
				rosterRequest: {
					"pageNo": 1,
					"pageSize": 10,
					"classId": null
				},
				classId:null
			}
		},
		onLoad(options) {
			this.className = options.class
			this.enterChoose = options.choose;
			this.classId = options.id
			this.requestClassRoster(options.id)
		},
		computed: {
			imageSrc: {
				get() {
					if (this.studentMessage.sex == 'WOMAN') {
						return "https://img.tukuppt.com/png_preview/00/05/38/DCoSdpcUVs.jpg!/fw/780"
					} else {
						return "https://img.tukuppt.com/png_preview/00/05/28/LjhCsodQxt.jpg!/fw/780"
					}
				}
			}
		},
		methods: {
			bindClick(e) {
				console.log('点击了' + (e.position === 'left' ? '左侧' : '右侧') + e.content.text + '按钮')
			},
			swipeChange(e, index) {
				console.log('当前状态：' + e + '，下标：' + index)
			},
			requestClassRoster(id) {
				this.rosterRequest.classId = id
				uni.$http.post(`/teacher/getStudentsByClassId`, this.rosterRequest).then(res => {
					if (res.data.code == 200) {
						this.endPage = res.data.data.endPage
						this.studentList = res.data.data.list
						if (this.rosterRequest.pageNo >= this.endPage) {
							this.shownodata = true
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
						this.shownodata = true
					}
				})
			},
			showStudent(item) {
				this.studentMessage = item
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
					id: this.studentMessage.id,
					studentNumber: this.studentMessage.studentNumber
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
			}
		},
		onReachBottom() {
			if (this.rosterRequest.pageNo >= this.endPage) {
				this.shownodata = true
				return
			}
			if (this.isloading) return;
			this.isloading = true
			this.rosterRequest.pageNo++;
			uni.$http.post(`/teacher/getStudentsByClassId`, this.rosterRequest).then(res => {
				if (res.data.code == 200) {
					console.log(res)
					uni.showToast({
						title: '加载中',
						duration: 500,
						icon: "loading"
					});
					this.studentList = [...this.studentList, ...res.data.data.list]
					this.isloading = false
				} else {
					this.msg.msgType = "error"
					this.msg.messageText = res.data.message
					this.$refs.message.open()
					this.isloading = false
				}
			}).catch(err => {
				this.msg.msgType = "error"
				this.msg.messageText = err
				this.$refs.message.open()
				this.isloading = false
			})
		}
	}
</script>

<style lang="scss">
	.class-roster-page {
		.remind {
			text-align: center;
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
