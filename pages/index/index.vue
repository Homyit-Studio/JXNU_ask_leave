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
			return {}
		},
		onLoad() {

		},
		methods: {
			toLogin(e) {
				let card = e.currentTarget.dataset.card;
				uni.navigateTo({
					url: "../login/login?card=" + card,
					animationType: 'pop-in',
					animationDuration: 200
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
		width: 660rpx;
		height: 250rpx;
		margin: 150rpx auto;
		background-size: 100rpx;
		background-color: #1b478e;
		border-radius: 20rpx;
		color: #f3fec3;
		text-shadow: #ffffce;
		font-size: 40rpx;
		box-shadow: 0px 1px 10px 2px rgba($color: #a5a4a4, $alpha: 0.5);

		&:hover {
			color: white;
		}
	}

	.card-text {
		margin-left: 40upx;
	}
</style>
