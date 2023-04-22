<template>
	<view>
		<view class="detail-status">
			<view>
				<uni-icons type="checkbox-filled" size="120" color="#1b478e" class="icon-style"></uni-icons>
			</view>
			<view>
				<text>已销假</text>
			</view>
		</view>
		<view class="details-card">
			<uni-card title="离校详情">
				<view><text decode="true">是否离校:&emsp;{{terminateDetails.depart === 'YES' ? '是' : terminateDetails.depart === 'NO' ? '否' : ''}}</text></view>
				<view>
					<view><text decode="true">离校时间:&emsp;{{terminateDetails.departTime}}</text></view>
					<view>
						<text decode="true">交通情况:&emsp;</text>
						<text class="reason-text">{{terminateDetails.departWay}}</text>
					</view>
				</view>
			</uni-card>
		</view>
		<view class="details-card">
			<uni-card title="返校详情">
				<view><text decode="true">是否返校:&emsp;{{terminateDetails.depart === 'YES' ? '是' : terminateDetails.depart === 'NO' ? '否' : ''}}</text></view>
				<view>
					<view><text decode="true">返校时间:&emsp;{{terminateDetails.backTime}}</text></view>
					<view>
						<text decode="true">交通情况:&emsp;</text>
						<text class="reason-text">{{terminateDetails.backWay}}</text>
					</view>
				</view>
			</uni-card>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				terminateDetails:{
					
				}
			}
		},
		methods: {
		},
		onLoad(item){
			uni.$http.get('/back/selectANote/' + item.id).then(res =>{
				// console.log(res)
				if(res.data.code === 200){
					this.terminateDetails = res.data.data
					// console.log(this.terminateDetails)
				}
			})
		}
	}
</script>

<style lang="scss">
	.detail-status {
			text-align: center;
			background-color: #fff;
			color: $uni-text-color;
			padding: 10rpx 0;
			margin: 50rpx;
		}

		.details-card {
			margin-top: 30rpx;
			padding-top: 0;
			text{
				color: $uni-text-color;
				font-size: 13px;
			}
			.reason-text{
				display: inline-block;
				width: 400rpx;
			}
			
		}
</style>
