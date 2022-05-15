<template>
	<view class="charts-box">
		<qiun-data-charts type="column" :chartData="chartData" />
	</view>
	<view>
		<!-- 提示信息弹窗 -->
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
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
				chartData: {
					categories: ["已销假", "待审核", "已拒绝", "销假过期", "上级审核", "待销假", "申请过期"],
					series: [{
						name: "数据统计",
						data: []
					}]
				},
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
						console.log(res.data.data)
						for (let item in data) {
							console.log(item)
							this.chartData.series[0].data.push(data[item])
						}
					} else {
						this.msg.msgType = "error"
						this.msg.messageText = res.data.message
						this.$refs.message.open()
					}
				}).catch(err => {
					this.msg.msgType = "error"
					this.msg.messageText = err.errMsg
					this.$refs.message.open()
				})
			}
		}
	};
</script>

<style lang="scss">
</style>
