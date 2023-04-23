-- hello world
print('Hellow World!!')

-- type()函数可以判断一个变量的类型
print(type('Hello Lua'))
print(type(function()
    return 1
end))

-- 声明局部变量 local

-- 声明字符串
local str = 'hello'
-- 字符串拼接用 ..
local appned = str .. 'lua'
print(append)
-- 声明数字
local age = 23
-- 声明boolean
local flag = true

-- 声明table类型
-- 数组(也作为map,key为角标)
local arr = { 'java', 'lua', 'python' }
-- map
local map = { name = 'yuyc', age = 22 }

-- table的下标从1开始
print(arr[1])

-- table作为map既可以以数组访问也可以以.访问
print(map[name])
print(map.name)

-- lua循环
for index, value in ipairs(arr) do
    print(index, value)
end
for key, value in pairs(map) do
    print(key, value)
end

-- lua 条件控制
local num = 100;
if num > 10 then
    print(num)
else
    print(num)
end


-- lua 函数
function pairsArr(arr)
    if not arr then
        return nil;
    end
    for index, value in ipairs(arr) do
        print(value)
    end
    return arr
end
print(pairsArr(arr))