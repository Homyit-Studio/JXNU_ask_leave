<template>
	<view class="modify-details-card">
		<view class="details-card">
			<uni-card title="审批情况" :is-shadow="false" class="details-card">
				<view>
					<text
						decode="true">辅导员意见:&emsp;{{leaveDetails.instructorOpinion === null ? '暂无' : leaveDetails.instructorOpinion}}</text>
				</view>
				<view>
					<text
						decode="true">学院&emsp;意见:&emsp;{{leaveDetails.secretaryOpinion === null ? '暂无' : leaveDetails.secretaryOpinion}}</text>
				</view>
				<view>
					<text
						decode="true">院长&emsp;意见:&emsp;{{leaveDetails.deanOpinion === null ? '暂无' : leaveDetails.deanOpinion}}</text>
				</view>
			</uni-card>
		</view>
		<view class="details-card">
			<uni-card title="请假详情" :is-shadow="false">
				<view><text decode="true">学&emsp;&emsp;号:&emsp;{{leaveDetails.studentNumber}}</text></view>
				<view><text decode="true">班&emsp;&emsp;级:&emsp;{{leaveDetails.majorAndClass}}</text></view>
				<view><text decode="true">目的地点:&emsp;{{leaveDetails.destination}}</text></view>
				<view><text
						decode="true">是否离校:&emsp;{{leaveDetails.depart === 'YES' ? '是' : leaveDetails.depart === 'NO' ? '否' : ''}}</text>
				</view>
				<view class="time-box">
					<view><text decode="true">开始时间:&emsp;{{leaveDetails.startTime}}</text></view>
					<view><text decode="true">结束时间:&emsp;{{leaveDetails.endTime}}</text></view>
					<view class="time-tag"><text>{{leaveDetails.days}}</text></view>
				</view>
				<view><text decode="true">前往方式:&emsp;{{leaveDetails.way}}</text></view>
				<view><text decode="true">联系号码:&emsp;{{leaveDetails.phoneNumber}}</text></view>
				<view><text decode="true">宿舍楼栋:&emsp;{{leaveDetails.dormitoryNumber}}</text></view>
				<view>
					<text decode="true">请假事由:&emsp;{{leaveDetails.reason}}</text>
				</view>
				<view>
					<text decode="true">附&emsp;&emsp;件:&emsp;</text>
					<view>
						<image v-for="item in imgs" :key="item.id" :src="item.url" mode="aspectFill"
							@click="preview(item)">
						</image>
					</view>
				</view>
			</uni-card>

			<view class="handle-buttons">
				<button type="primary" class="agree-button" @click="modifyAgain">更正信息</button>
			</view>
		</view>

		<view>
			<!-- 信息弹窗 -->
			<uni-popup ref="modifyMessagePopup" background-color="#fff" type="center">
				<view class="popup-content">
					<uni-forms ref="form" :modelValue="modifyMessage">
						<uni-forms-item label="是否离校" name="depart">
							<uni-data-checkbox v-model="modifyMessage.depart" :localdata="options"></uni-data-checkbox>
						</uni-forms-item>
						<uni-forms-item label="开始时间" name="startTime">
							<uni-datetime-picker type="datetime" v-model="modifyMessage.startTime" :border="false"
								:clear-icon="false" placeholder="请输入查找的开始时间" />
						</uni-forms-item>
						<uni-forms-item label="结束时间" name="endTime">
							<uni-datetime-picker type="datetime" v-model="modifyMessage.endTime" :border="false"
								:clear-icon="false" placeholder="请输入查找的结束时间" />
						</uni-forms-item>
						<uni-forms-item label="是否同意" name="opinionEnum">
							<uni-data-checkbox v-model="modifyMessage.opinionEnum" :localdata="options">
							</uni-data-checkbox>
						</uni-forms-item>
						<uni-forms-item label="负责人" name="checkpeople" required
							v-if="modifyMessage.opinionEnum == 'YES' && identity == 'INSTRUCTOR' && leaveDetails.level != 'INSTRUCTOR'">
							<uni-data-checkbox multiple v-model="modifyMessage.leaderNumber" :localdata="nextPeople" />
						</uni-forms-item>

						<uni-forms-item name="advice" label="审批建议">
							<uni-easyinput type="textarea" v-model="modifyMessage[identityOpintion]"
								placeholder="请输入审核意见" />
						</uni-forms-item>
					</uni-forms>
					<button @click="modifyConfirm">确定</button>
				</view>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				leaveDetails: {},
				modifyMessage: {
					id:null,
					startTime: "",
					endTime: "",
					depart: "",
					opinionEnum: "",
					levelEnum: ""
				},
				options: [{
						text: "是",
						value: "YES"
					},
					{
						text: "否",
						value: "NO"
					}
				],
				identityOpintion: "",
				identity: null,
				nextPeople : []
			}
		},
		onLoad(options) {
			this.requestDetail(options.id)
			let identity = uni.getStorageSync('identity')
			this.identityOpintion = identity.toLowerCase() + "Opinion"
			this.identity = identity
			this.getLeaders()
		},
		methods: {
			requestDetail(id) {
				uni.showLoading({
					title: "正在请求中"
				})
				uni.$http.get('/leave/selectANote/' + id).then(res => {
					if (res.data.code === 200) {
						this.leaveDetails = res.data.data
						console.log(this.leaveDetails)
						const {
							startTime,
							endTime,
							depart,
							opinionEnum,
							level,
							id
						} = res.data.data
						this.modifyMessage = {
							startTime,
							endTime,
							depart,
							opinionEnum,
							levelEnum:level,
							id
						}
						this.modifyMessage[this.identityOpintion] = ''
						uni.hideLoading();
						//le.log(this.leaveDetails)
					} else {
						uni.showToast({
							title: "请求错误",
							icon: "error"
						})
					}

				}).catch(err => {
					uni.showToast({
						title: "网络出小差了，请重试",
						icon: "error"
					})
				})
			},
			modifyAgain(){
				this.$refs.modifyMessagePopup.open()
			},
			modifyConfirm(){
				console.log(this.modifyMessage)
				uni.showLoading({
					title:"更正中"
				})
				if(this.modifyMessage.opinionEnum == 'YES' && this.modifyMessage[this.identityOpintion] == ""){
					console.log("空")
					this.modifyMessage[this.identityOpintion] = "同意"
				}else if(this.modifyMessage.opinionEnum == 'NO' && this.modifyMessage[this.identityOpintion] == ""){
					this.modifyMessage[this.identityOpintion] = "拒绝"
				}
				if (this.modifyMessage.opinionEnum == 'YES' && this.modifyMessage.leaderNumber.length != 0) {
					this.modifyMessage.leaderNumber = this.modifyMessage.leaderNumber.join()
				}
				console.log(this.modifyMessage)
				uni.$http.post("/leave/updateTheNote", this.modifyMessage).then(res => {
					if (res.data.code == 200) {
						this.leaveDetails = res.data.data
						uni.showToast({
							title:"更正成功"
						})
						this.$refs.modifyMessagePopup.close()
					} else {
						this.shownodata = true
						uni.showToast({
							icon:"error",
							title:res.data.message
						})
					}
				})
			},
			//获取负责人
			getLeaders() {
				if (this.nextPeople.length == 0) {
					uni.$http.get("/user/getAllLeaders").then(res => {
						if (res.data.code == 200) {
							for (let item in res.data.data) {
								let peopleobj = {}
								peopleobj.text = item;
								peopleobj.value = res.data.data[item]
								this.nextPeople.push(peopleobj)
								// this.$refs.inputDialog.open()
							}
						} else {
							uni.showToast({
								title:"网络出小差了，请重试",
								icon:"error"
							})
						}
					})
				}
			},
			
		}
	}
</script>

<style lang="scss">
	.modify-details-card {
		.details-card {
			.uni-card {
				margin-top: 0;
				padding-top: 0;


				.time-box {
					position: relative;

					.time-tag {
						position: absolute;
						right: 0;
						top: 12rpx;
						font-size: 12px;
						padding: 10rpx;
						color: #fff;
						background-color: #1b478e;
					}
				}

				image {
					width: 80px;
					height: 80px;
				}
			}

			.handle-buttons {
				display: flex;
				justify-content: center;
				padding: 50rpx 0;

				button {
					width: 300rpx;
					height: 60rpx;
					line-height: 60rpx;
					font-size: $jxnu-font-14;
				}

				.agree-button {
					background-color: $jxnu-bg-color;
				}
			}
		}

		.popup-content {
			padding: 30rpx;

			border-radius: 50rpx;
			width: 600rpx;

			button {
				height: 65rpx;
				width: 200rpx;
				font-size: 14px;
				background-color: $jxnu-bg-color;
				line-height: 65rpx;
				color: #fff;
			}
		}
	}
</style>
