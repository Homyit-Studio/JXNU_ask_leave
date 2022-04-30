<template>
	<view class="content">
		<view class="identity-card">
			<uni-icons custom-prefix="iconfont" type="icon-xuesheng" size="40" color="#f3fec3"></uni-icons>
			<text class="card-text">我是学生</text>
		</view> 
		<view class="identity-card">
			<uni-icons custom-prefix="iconfont" type="icon-shangkelaoshiwo" size="40" color="#f3fec3"></uni-icons>
			<text class="card-text">我是老师</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		onLoad() {

		},
		methods: {

		}
	}
</script>

<style>
	.identity-card {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 660upx;
		height: 250upx;
		margin: 100upx auto;
		background: url("https://www.jxnu.edu.cn/_upload/site/00/05/5/logo.png") 500upx 50upx no-repeat;
		background-color: #a13838;
		border-radius: 20upx;
		color: #f3fec3;
		text-shadow: #ffffce;
		font-size: 40upx;
		box-shadow: 0px 1px 10px 2px rgba($color: #a5a4a4, $alpha: 0.5);
	}
	.identity-card:hover{
		color: white;
	}
	.card-text{
		margin-left: 40upx;
	}
</style>
