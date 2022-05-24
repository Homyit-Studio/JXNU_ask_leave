<template>
	<view class="finish-record">
		<view class="card-remind">
			<uni-card :isFull="true">
				<text>
					以下数据为计信学院的请假相关数据预览(默认为一星期)，可前往请假记录查看详情：
				</text>
				<view class="nav-button">
					<navigator url="../showGradeLeave/showGradeLeave">请假记录>></navigator>
				</view>
				<view>
					<text>{{dateChoose.startTime}}至{{dateChoose.endTime}}的报表</text>
				</view>
			</uni-card>
		</view>
		<view class="charts-box">
			<qiun-data-charts :ontouch="true" :opts="opts" :onmovetip="true" :canvas2d="true" canvasId="uhfduhf9842342" type="pie"
				:chartData="chartData" />
		</view>
		<uni-card isFull="true">
			<view>
				<text>
					可选择起始日期查看该时间段的报表：
				</text>
				<view class="nav-button" @click="datePopup">
					<text>选择时间</text>
				</view>
			</view>
			<view>
				<text>
					以下为年级分类假条报表，可切换年级进行查看：
				</text>
			</view>
			<view>
				<text>{{dateChoose.startTime}}至{{dateChoose.endTime}}期间{{gradevalue}}级的请假记录报表</text>
			</view>
		</uni-card>
		<view class="grade-select">
			<uni-data-select v-model="gradevalue" :localdata="gradeSelect" @change="changeGrade" label="年级">
			</uni-data-select>
		</view>
		<view class="charts-box">
			<qiun-data-charts type="column" :ontouch="true" :onmovetip="true" :canvas2d="true"
				canvasId="uxjaeuhf9842342" :chartData="gradeChartData" />
		</view>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="dateMessage" background-color="#fff" type="center" class="data-message">
				<uni-group>
					<uni-forms ref="form" :modelValue="changingDate" validateTrigger="submit">
						<uni-forms-item required label="选择起始时间">
							<uni-datetime-picker type="datetime" v-model="dateChoose.startTime" :border="false"
								:clear-icon="false" placeholder="选择起始日期和时间" />
						</uni-forms-item>
						<uni-forms-item required label="选择结束时间">
							<uni-datetime-picker type="datetime" v-model="dateChoose.endTime" :border="false"
								:clear-icon="false" placeholder="选择结束日期和时间" />
						</uni-forms-item>
						<button @click="changeDate">获取报表</button>
					</uni-forms>
				</uni-group>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				chartData: {},
				dataMap: {
					"REPORT_EXPIRED": "销假过期",
					"PROCESSING": "处理中",
					"PROCESSED": "已销假",
					"WAIT_REPORT": "待销假",
					"FAILURE": "已拒绝",
					"APPLY_EXPIRED": "申请过期"
				},
				gradevalue: "2021",
				gradeSelect: [{
						value: "2021",
						text: "2021级"
					},
					{
						value: "2020",
						text: "2020级"
					},
					{
						value: "2019",
						text: "2019级"
					},
					{
						value: "2018",
						text: "2018级"
					},
					{
						value: "3021",
						text: "研一"
					},
					{
						value: "3020",
						text: "研二"
					},
					{
						value: "3019",
						text: "研三"
					},
					{
						value: "1018",
						text: "留学生四年级"
					},
					{
						value: "1019",
						text: "留学生三年级"
					}
				],
				dateChoose: {
					"startTime": "",
					"endTime": ""
				},
				opts: {
					color: ["#91CB74", "#1890FF", "#FAC858", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4", "#EE6666",
						"#ea7ccc"
					],
					padding: [5, 5, 5, 5],
					extra: {
						pie: {
							activeOpacity: 0.5,
							activeRadius: 10,
							offsetAngle: 0,
							labelWidth: 15,
							border: false,
							borderWidth: 3,
							borderColor: "#FFFFFF"
						}
					}
				},
				gradeChartData: {}
			};
		},
		onReady() {
			this.dateChoose.startTime = this.getFormatDate(2)
			this.dateChoose.endTime = this.getFormatDate()
			this.requestAllCounts()
			this.getGradeData(this.gradevalue)
		},
		methods: {
			datePopup() {
				this.$refs.dateMessage.open()
			},
			changeDate() {
				this.requestAllCounts()
				this.getGradeData(this.gradevalue)
			},
			requestAllCounts() {
				console.log(this.dateChoose)
				uni.$http.post("/leave/allCounts", this.dateChoose).then(res => {
					if (res.data.code == 200) {
						let data = res.data.data
						console.log(res.data.data)
						this.chartData.categories = []
						this.chartData.series = [{
							name: "请假统计",
							data: []
						}]
						this.$refs.dateMessage.close()
						for (let item in data) {
							this.chartData.categories.push(this.dataMap[item])
							let catalog = {}
							catalog.name = this.dataMap[item]
							catalog.value = data[item]
							this.chartData.series[0].data.push(catalog)
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "请求错误"
						this.$refs.message.open()
					}
				})
			},
			clickdata(e) {
				console.log(e)
			},
			changeGrade(grade) {
				this.getGradeData(grade)
			},
			getGradeData(grade) {
				uni.$http.post(`/leave/allCountsFroGradeId`, {
					"startTime": this.dateChoose.startTime,
					"endTime": this.dateChoose.endTime,
					"gradeId": grade
				}).then(res => {
					if (res.data.code == 200) {
						let data = res.data.data
						console.log(res.data.data)
						this.gradeChartData.categories = []
						this.gradeChartData.series = [{
							name: "年级统计",
							data: []
						}]
						for (let item in data) {
							console.log(item)
							this.gradeChartData.categories.push(this.dataMap[item])
							this.gradeChartData.series[0].data.push(data[item])
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "请求错误"
						this.$refs.message.open()
					}
				})
			},
			getFormatDate(choose) {
				var date = new Date();
				var sign1 = "-";
				var sign2 = ":";
				var year = date.getFullYear() // 年
				var month = date.getMonth() + 1; // 月
				var day = date.getDate(); // 日
				var hour = date.getHours(); // 时
				var minutes = date.getMinutes(); // 分
				var seconds = date.getSeconds() //秒
				// 给一位数数据前面加 “0”
				if (month >= 1 && month <= 9) {
					month = "0" + month;
				}
				if(choose == 2){
					day -= 7
				}
				if (day >= 0 && day <= 9) {
					day = "0" + day;
				}
				if (hour >= 0 && hour <= 9) {
					hour = "0" + hour;
				}
				if (minutes >= 0 && minutes <= 9) {
					minutes = "0" + minutes;
				}
				if (seconds >= 0 && seconds <= 9) {
					seconds = "0" + seconds;
				}
				var currentdate = year + sign1 + month + sign1 + day + " " + hour + sign2 + minutes + sign2 + seconds;
				return currentdate;
			},
		}
	};
</script>

<style lang="scss">
	.finish-record {
		.charts-box {
			margin-top: 20px;
		}

		.card-remind {
			.uni-card {
				font-size: 12px;
			}
		}

		.nav-button {
			width: 80px;
			height: 20px;
			display: inline-block;
			text-align: center;
			font-size: 12px;
			background-color: $jxnu-bg-color;
			color: #fff;
			border-radius: 10px;
		}

		.grade-select {
			display: flex;
			justify-content: center;
		}

		.data-message {
			button {
				width: 400rpx;
				height: 30px;
				line-height: 30px;
				margin-top: 20px;
				background-color: $jxnu-bg-color;
				color: #fff;
			}
		}
	}
</style>
