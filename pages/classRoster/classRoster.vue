<template>
	<view class="class-roster-page">
		<uni-card is-full :is-shadow="false" class="remind">
			<text>右滑显现班级成员操作按钮</text>
		</uni-card>
		<view class="student-title"><text>{{className}}</text></view>
		<uni-card v-for="(item, i) in studentList" :key="item.classNumber">
			<view class="message-card" @click="showStudent(item)">
				<view class="student-message">
					<image :src="imageSrc"></image><text>{{item.username}}</text>
				</view>
				<view><text>学号:{{item.studentNumber}}</text></view>
			</view>
		</uni-card>
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="studentPopup" background-color="#fff" type="bottom">
				<view class="popup-content">
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
						<button type="default" class="cancel-button" @click="cancelSubmit">取消</button>
						<button type="default" class="revise-button" @click="reviseSubmit">确认</button>
						<button type="default" class="revise-button" @click="reviseSubmit">确认</button>
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
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//没有更多数据提醒
				shownodata: false,
				enterChoose:null,
				//节流阀
				isloading: false,
				className: null,
				//数据总数
				endPage: null,
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				imageSrc: "data:image/jpeg;base64,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",
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
				}
			}
		},
		onLoad(options) {
			this.className = options.class
			this.enterChoose = options.choose;
			console.log(options)
			this.requestClassRoster(options.id)
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
						console.log(res)
						uni.showToast({
							title: '加载中',
							duration: 500,
							icon: "loading"
						});
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
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err.errMsg
					this.$refs.message.open()
					this.shownodata = true
				})
			},
			showStudent(item) {
				this.studentMessage = item
				this.$refs.studentPopup.open()
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
						width: 220rpx;
						height: 60rpx;
						line-height: 60rpx;
						font-size: $jxnu-font-14;
					}
		
					.revise-button {
						background-color: $jxnu-bg-color;
					}
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
