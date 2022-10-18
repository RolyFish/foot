

## 数组

```
一维数组
int[] nums = new int[10];

二维数组
int[][] array = new int[2][2]
array[0] 存的是数组对象
```

```java
///冒泡排序   :每一次内循环都是将 最大的数排到最后面
int temp = 0;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }

优化：
    一般情况下复杂度为：O(N^2)  即是时有序的数组也会进行比较 
    最优复杂度：O(N)[有序数组]
    int temp = 0;
	int flag = true;
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {
                    temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                    flag = false;
                }
            }
            if(flag == true){
                break;//当这一圈走完  为发生数据交换时直接跳出循环
            }
        }
```

## 稀疏数组



==适用于数组中存在较多的默认值（0）的数组==

| 行数 | 列数 | 有效元素value              |
| ---- | ---- | -------------------------- |
| 3    | 4    | 2个有效数据                |
| 0    | 1    | 1（第一行第二列的值为1）   |
| 2    | 2    | 4（第三行，第三列的值为4） |

```java
//代码实现  数组为 array[i][j]  稀疏数组为 array2[sum][3]
int sum = 0;
for(int i =0 ; i<array.length;i++){
    for(int j = 0;j<array[0].length;j++){
        if(array[i][j] != 0){
            sum++;
        }
    }
}
array2[0][0] = i;
array2[0][1] = j;
array2[0][2] = sum;
int row = 1;
for(int i =0 ; i<array.length;i++){
    int col = 0;
    for(int j = 0;j<array[0].length;j++){
        if(array[i][j] == 0){
           array2[row][col++] = i;
           array2[row][col++] = j;
           array2[row][col] = array[i][j];
        }
    }
}
```

