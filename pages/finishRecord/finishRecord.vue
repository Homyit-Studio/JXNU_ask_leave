<template>
	<view class="finish-record">
		<view class="card-remind">
			<uni-card :isFull="true">
				<text>
					以下数据为计信学院最近一星期的请假相关数据预览，可前往请假记录查看详情。
				</text>
				<view class="nav-button">
					<navigator url="../showGradeLeave/showGradeLeave">请假记录>></navigator>
				</view>
			</uni-card>
		</view>
		<view class="charts-box">
			<qiun-data-charts :ontouch="true" :onmovetip="true" :canvas2d="true" canvasId="uhfduhf9842342" type="column"
				:chartData="chartData" />
		</view>
		<view>
			<text>
				年级分类假条：
			</text>
		</view>
		<view class="grade-select">
			<uni-data-select v-model="gradevalue" :localdata="gradeSelect" @change="changeGrade" label="年级">
			</uni-data-select>
		</view>
		<view class="charts-box">
			<qiun-data-charts type="pie" :opts="opts" :ontouch="true" :onmovetip="true" :canvas2d="true"
				canvasId="uxjaeuhf9842342" :chartData="gradeChartData" />
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
					}
				],
				opts: {
					color: ["#91CB74", "#1890FF","#FAC858", "#73C0DE", "#3CA272", "#FC8452", "#9A60B4","#EE6666",
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
				gradeChartData:{}
			};
		},
		onReady() {
			this.requestAllCounts()
			this.getGradeData(this.gradevalue)
		},
		methods: {
			requestAllCounts() {
				uni.$http.get("/leave/allCounts").then(res => {
					if (res.data.code == 200) {
						let data = res.data.data
						console.log(res.data.data)
						this.chartData.categories = []
						this.chartData.series = [{
							name: "请假统计",
							data: []
						}]
						for (let item in data) {
							console.log(item)
							this.chartData.categories.push(this.dataMap[item])
							this.chartData.series[0].data.push(data[item])
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
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
				uni.$http.get(`/leave/allCounts/${grade}`).then(res => {
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
							let catalog={}
							catalog.name = this.dataMap[item]
							catalog.value = data[item]
							this.gradeChartData.series[0].data.push(catalog)
						}
						console.log(this.chartData.series[0].data)
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
					}
				})
			}
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
	}
</style>
