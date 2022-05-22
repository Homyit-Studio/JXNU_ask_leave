<template>
	<view class="person-record">
		<view class="card-remind">
			<uni-card :isFull="true">
				<text>
					以下数据为与您相关的近一个星期请假数据的预览，可前往审批假条可查看详情。
				</text>
				<view class="nav-button">
					<navigator url="../handleLeave/handleLeave">审批假条>></navigator>
				</view>
			</uni-card>
		</view>
		<view class="charts-box">
			<qiun-data-charts type="column" :ontouch="true" :onmovetip="true" :canvasId="uhfeewuhf9842342"
				:chartData="chartData" />
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
					"PROCESSING": "待审核",
					"PROCESSED": "已销假",
					"WAIT_REPORT": "待销假",
					"FAILURE": "已拒绝",
					"APPLY_EXPIRED": "申请过期",
					"TRANSMIT": "上级审核"
				}
			};
		},
		onLoad() {
			this.requestAllCounts()
		},
		methods: {
			requestAllCounts() {
				uni.$http.get("/leave/allCountsForPerson").then(res => {
					if (res.data.code == 200) {
						let data = res.data.data
						this.chartData.categories = []
						this.chartData.series = [{
							name: "请假统计",
							data: []
						}]
						console.log(res.data.data)
						for (let item in data) {
							console.log(item)
							this.chartData.categories.push(this.dataMap[item])
							this.chartData.series[0].data.push(data[item])
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = "请求错误"
						this.$refs.message.open()
					}
				})
			}
		}
	};
</script>

<style lang="scss">
	.person-record{
		.nav-button{
			width: 80px;
			height: 20px;
			text-align: center;
			font-size: 12px;
			background-color: $jxnu-bg-color;
			color: #fff;
			border-radius: 10px;
		}
	}
</style>
