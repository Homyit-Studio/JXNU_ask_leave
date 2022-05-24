<template>
	<view class="contact-us">
		<view class="homyit">
			<cover-image src="https://homyit-studio.github.io/Homyit-Guide/images/Homyit.png" class="homyit-image"></cover-image>
			<text>HOMYIT STUDIO</text>
		</view>
		<uni-list>
			<uni-list-item  title="问题反馈">
				<template v-slot:body>
					<view><text>系统中如果出现请求错误，系统卡顿以及其他问题点击联系客服，进行问题反馈。</text></view>
					<view class="contact">
						<button open-type="contact" class="contact-button" size="mini">联系客服</button>
					</view>
				</template>
			</uni-list-item>
		</uni-list>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
	.contact-us{
		.homyit{
			margin: 20px 0;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;
			.homyit-image{
				width: 60px;
				height: 60px;
			}
		}
		.contact{
			display: flex;
			justify-content: center;
			.contact-button{
				margin: 10px;
				background-color: $jxnu-bg-color;
				color: #fff;
			}
		}
	}
</style>
