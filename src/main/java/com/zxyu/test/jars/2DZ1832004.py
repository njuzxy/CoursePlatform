from pyspark import SparkContext
from pyspark.streaming import StreamingContext
import heapq

class TopK(object):
    def __init__(self,KVALUE):
        self.KVALUE = KVALUE
        self.Kdata = []

    def push(self, element):
        if len(self.Kdata) < self.KVALUE:
            heapq.heappush(self.Kdata, element)
        else:
            heapq.heappushpop(self.Kdata, element)

    def print(self,path):
        self.Kdata.sort(reverse = True)
        with open(path, 'w') as output:
            for pair in self.Kdata:
                output.write(pair[1] + '\n')

    def getData(self):
        return self.Kdata

def getPairs(line):
    temp = line.split(",")
    return (temp[0],float(temp[1]))

def pushIntoHeap(partition, topKObject):
    for record in partition:
        topKObject.push(record)

def getTopK(iterator,KVALUE):
    tempTopK = TopK(KVALUE)
    for x in iterator:
        tempTopK.push(x)

    return tempTopK.getData()

with open('/home/homework02/variables.txt','r') as configFile:
    KValue = int(configFile.readline())
    dataPath = configFile.readline().strip('\n')
    outputPath = configFile.readline().strip('\n')

sc = SparkContext("192.168.68.11:7077", "ComputingTopK")
ssc = StreamingContext(sc,1)

dataFile = sc.textFile(dataPath)
pairs = dataFile.map(lambda line: getPairs(line))

reducedPairs = pairs.reduceByKey(lambda a,b: a+b)

# value and id pair
gotPairs = reducedPairs.map(lambda a: (a[1],a[0]))
# get topK of each partition
gotTopKEveryPartition = gotPairs.mapPartitions(lambda iterator: getTopK(iterator, KValue))

finalTopK = gotTopKEveryPartition.collect()
# construct the final topK into heap
TOPK = TopK(KValue)
for element in finalTopK:
    TOPK.push(element)
# reverse the heap and output
TOPK.print(outputPath+'DZ1832004.txt')

