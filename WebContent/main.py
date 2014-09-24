__author__ = 'HanSangyun'

import os
import sys
import hashlib
import glob
import time
import CloudConvert
import _thread

lib_path = os.path.abspath('..')
sys.path.append(lib_path)



#apikey = open("api_key.txt", "r").read().strip()
#apikey = "PWuKDNZ2j7S-Wmz6gthre0ttUhaEgBCeG69g8zvoW-lmsGwISHqgbwSiYEU_aesENHTWrC0zXu23CjIpiKcRYQ"


def Update(ApiKeyList):
    BLOCKSIZE = 65536

    oldSet = set()

    while 1:
        nameList = glob.glob('AudioDatas/*')

        newSet = set()

        for name in nameList:
            hasher = hashlib.md5()

            try:
                with open(name, 'rb') as afile:
                    buf = afile.read(BLOCKSIZE)
                    while len(buf) > 0:
                        hasher.update(buf)
                        buf = afile.read(BLOCKSIZE)
            except:
                pass

            fhash = hasher.hexdigest()
            newSet.add((name,fhash))

        uploadSet = newSet.difference(oldSet)

        for uploadFile in uploadSet:
            inputFile = uploadFile[0]
            print(inputFile)

            if inputFile[len(inputFile)-3:len(inputFile)] == "wav":
                print(inputFile[len(inputFile)-3:len(inputFile)])
                continue
            else:
                outputFile = inputFile[0:len(inputFile)-4]
                print("convert : " + outputFile)
                #convert(inputFile, outputFile + ".wav", ApiKeyList)
                _thread.start_new_thread(convert,(inputFile, outputFile+".wav", ApiKeyList))
                _thread.start_new_thread(convert,(inputFile, outputFile+".wav", ApiKeyList))
                _thread.start_new_thread(convert,(inputFile, outputFile+".wav", ApiKeyList))
                _thread.start_new_thread(convert,(inputFile, outputFile+".wav", ApiKeyList))
                _thread.start_new_thread(convert,(inputFile, outputFile+".wav", ApiKeyList))

        oldSet = set(newSet)

        #time.sleep(1)


def convert(inputFile, outputFile, ApiKeyList):


    for ApiIndex in range(len(ApiKeyList)):
        try:
            process = CloudConvert.ConversionProcess(ApiKeyList[ApiIndex])
            CloudConvert.CloudConvert.is_possible("amr", "wav")
            process.init(inputFile, outputFile)
            break
        except CloudConvert.ConversionProcessException:
            continue


    '''
    while CloudConvert.CloudConvert.is_possible("amr", "wav") == False:
        ApiIndex = ApiIndex + 1
        process = CloudConvert.ConversionProcess(ApiKeyList[ApiIndex])
        print("api key index : " + ApiIndex)'''

    print("start")
    #process.start()
    options = {"audio_bitrate": "32"}
    process.Converting(options)


    print("waiting")
    process.wait_for_completion()

    print("Saving")
    process.save()

    with open(process.fromfile, "rb") as f:
        a = hashlib.md5(f.read()).hexdigest()

    with open(process.tofile, "rb") as f:
        b = hashlib.md5(f.read()).hexdigest()




if __name__ == '__main__':
    ApiKeyList = open("api_key.txt", "r").readlines()
    for index in range(len(ApiKeyList)):
        ApiKeyList[index] = ApiKeyList[index].strip()

    Update(ApiKeyList)
