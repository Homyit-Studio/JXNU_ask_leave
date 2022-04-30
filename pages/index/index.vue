<template>
	<view class="content">
		<view class="identity-card" @tap="toLogin" data-card="student">
			<uni-icons custom-prefix="iconfont" type="icon-xuesheng" size="40" color="#f3fec3"></uni-icons>
			<text class="card-text">我是学生</text>
		</view> 
		<view class="identity-card" @tap="toLogin" data-card="teacher">
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
			toLogin(e){
				let card = e.currentTarget.dataset.card;
				uni.navigateTo({
					url : "../login/login?card=" + card 
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.identity-card {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 660upx;
		height: 250upx;
		margin: 150upx auto;
		background-size: 100upx;
		background-color: #1b478e;
		border-radius: 20upx;
		color: #f3fec3;
		text-shadow: #ffffce;
		font-size: 40upx;
		box-shadow: 0px 1px 10px 2px rgba($color: #a5a4a4, $alpha: 0.5);
		&:hover{
			color: white;
		}
	}
	.card-text{
		margin-left: 40upx;
	}
</style>
