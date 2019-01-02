from pyspark import SparkContext, SparkConf

conf = SparkConf().setAppName('Top K').setMaster('spark://192.168.68.11:7077')#提交版spark://shzx002:18080
# conf = SparkConf().setAppName('Top K').setMaster('local')#本地测试
sc = SparkContext(conf=conf)

f = open("/home/homework02/variables.txt")#提交版
# f = open("variables.txt")#本地测试
k = int(f.readline())
data_dir = f.readline().strip('\n')
p = f.readline().strip().strip('\n')

lines = sc.textFile(data_dir)
words = lines.map(lambda line: (line.split(",")[0].replace("id", ''), int(line.split(",")[1].replace("id", ''))))#先根据“，”split，然后去掉“id”
counts = words.reduceByKey(lambda a, b: int(a) + int(b))#reduce
sort = counts.sortBy(lambda pair: pair[1], False)#根据value进行排序
map = sort.map(lambda pair: pair[0])#只取key值
topK = map.take(k)#取Top K

str = ''
for t in topK:
    str = str + "id" + t + '\n'

#将最后的计算结果写入txt文件
output_dir = p + "DZ1832003.txt"
output = open(output_dir, 'w')
output.write(str)
output.close()
# map.saveAsTextFile("result.txt")
# print(result.take(50))

# result = sort.collect()
# for(id, count) in result:
#     print(id, count)

sc.stop()

