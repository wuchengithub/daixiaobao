


int main(int argc ,char[] avgs) {
	@autoreleasepool {
		//结构体的定义和结构体变量的初始化
		struct point {
			int i;
			int j;

		} p1 {1,2};
		
		
		//使用typeof关键字重新定义类型
		typedef struct point WCpoint;
		
		WCpoint p2 = {2,3}；
		
		//定义一个结构体数组
		WCpoint[] arr = { {3,4},{3,6}};
		
		arr[1].i = 3;
		
		int arrLen = sizeof(arr)/sizeof(arr[0]);
		//定义块变量
		(void) ^(block)(WCpoint[]);
		//讲块赋值块变量point
		point ^(WCpoint[] arr) {
			for (WCpoint* p = arr; p < arr + arrLen + ; p++) {
				NSLog(@"is isisisi &@:" + *p.i);
			}
		}
		
		//调用块
		point(arr);
		
	}
	
}
