<template>
	<view class="pointer">
		<template v-for="(item,index) in data" :key="item.value">
			<template v-if="!item.children || !item.children.length">
				<view class="menu-item">
					<uni-menu-item :index="item">
						<view>
							<text>{{item.text}}</text>
						</view>
					</uni-menu-item>
					<view class="menu-total">{{item.total > 99 ? "99+" : item.total}}</view>
				</view>
				<!-- <uni-badge class="barage-total" :text="item.total" absolute="rightTop" size="small">
					<uni-menu-item :index="item">
						<view>
							<text>{{item.text}}</text>
						</view>
					</uni-menu-item>
				</uni-badge> -->
			</template>
			<uni-sub-menu v-else :index="item">
				<template v-slot:title>
					<text>{{item.text}}</text>
				</template>
				<uni-menu-sidebar class="item-bg" :data="item.children" :key="item._id" />
			</uni-sub-menu>
		</template>
	</view>
</template>

<script>
	import uniMenuSidebar from '../uni-menu-sidebar/uni-menu-sidebar.vue'
	export default {
		name: 'uniMenuSidebar',
		props: {
			data: {
				type: Array,
				default () {
					return []
				}
			}
		},
		data() {
			return {};
		},
		computed: {

		},
		methods: {

		}
	}
</script>

<style lang="scss">
	@import url("uni-icons.css");

	.menu-item {
		position: relative;

		.menu-total {
			position: absolute;
			right: 10px;
			top: 2px;
			min-width: 20px;
			height: 20px;
			text-align: center;
			font-size: 12px;
			box-sizing: border-box;
			background-color: #1b478e;
			line-height: 20px;
			color: #fff;
			border-radius: 50%;
			z-index: 100;
			}
			// .barage-total{
			// 	background-color: #1b478e;
			// 	color: #fff;
			// 	z-index: 100;
			// }
		
		
	}
</style>
