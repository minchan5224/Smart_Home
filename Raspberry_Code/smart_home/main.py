# -*- coding:utf-8 -*-
from bs4 import BeautifulSoup# 패키지 설치
import requests # 패키지 설치
import urllib.request
import time
import json
import os
from servo import move, move_g

def php_read(url_str): #read 하나로 통합. -> 서버에 저장된 사용자 요청값 읽어옴
    target_url ='https://capstoneteamh.000webhostapp.com/'+url_str
    try : 
        html = urllib.request.urlopen(target_url).read()
    except urllib.error.HTTPError :
        return php_read(url_str)
    i = str(BeautifulSoup(html, 'html.parser'))
    print(url_str+"     "+i)
    return i

def data_save(): #로컬에도 Smart home 의 상태 저장. (Json형식) -> 로컬 저장위치에 값 저장
    web_value = True
    while web_value:
        gas_d = php_read("gas.txt")
        camera_d = php_read("camera.txt")
        dict={'gas': gas_d ,'camera' : camera_d}
        print(dict)
        with open('home_info.json','w+',encoding='utf-8')as make_file: #.json파일 쓰기형식, utf-8 로 인코딩해 오픈
            json.dump(dict, make_file, ensure_ascii=False, indent="\t")
        web_value = False

def requests_php(action_num, verify, select): #동작 수행과 서버에 동작수행후 값 전달. -> 서버 최신화.
    if(select == 1): #가스밸브
        if(action_num == 1):
            data = {'value': verify, 'choice' : '1', 'select' : '1'}
            requests.post('https://capstoneteamh.000webhostapp.com/ctrl.php', data=data)
            print("잠김",verify,"select 수정")
            move_g(float(3.5))
            return str(1)
        elif(action_num == 2):
            data = {'value': verify, 'choice' : '1', 'select' : '1'}
            requests.post('https://capstoneteamh.000webhostapp.com/ctrl.php', data=data)
            print("열림",verify)
            move_g(float(7))
            return str(2)

    elif(select == 2):
        data = {'value': verify, 'choice' : '2', 'select' : '1'}
        requests.post('https://capstoneteamh.000webhostapp.com/ctrl.php', data=data)
        print(verify," 만큼 회전")
        verify_c = str(move(int(verify)))#새로운 로컬 저장값
        if(verify_c == verify):
            print("동작완료")
        return verify_c
    
    elif(select == 3):
        data = {'value': verify, 'choice' : '2', 'select' : '1'}
        requests.post('https://capstoneteamh.000webhostapp.com/ctrl.php', data=data)
        print("기존 동작과 같은 동작 요청")
        return verify

def main():
    web_value = True
    try_num=0
    if os.path.exists('home_info.json'):
        try_num = 1
    else :
        try_num = 0
    
    while web_value:
        target_url ='https://capstoneteamh.000webhostapp.com/select.txt'
        try : 
            html = urllib.request.urlopen(target_url).read()
        except urllib.error.HTTPError :
            continue
        soup = BeautifulSoup(html, 'html.parser')
        i = str(soup.find("div", {"class":"read"}))[18] #사용자의 요청이 들어왔는지 확인하기 위한값.
        if try_num == 0:
            try_num += 1 #첫동작 구분하기 위한 int
            data_save()
            
        if (i == '2') :
            print(i,"사용자의 카메라 제어요청 감지")
            with open('home_info.json') as data_file:
                dict = json.load(data_file)
            choice_c = dict.get('camera')#로컬 저장값
            verify_c = php_read("camera.txt") #웹 저장값
            if(verify_c != choice_c): # 1 가스 잠김 수행, 2 카메라 회전 동작
                #카메라 로컬 저장값과 카메라 웹 저장값이 다를때 -> 사용자의 요청이 반영되지 않은상태 -> 동작수행
                print("사용자의 카메라 동작 요청")
                verify_c = requests_php(int(verify_c), verify_c, 2)

            else:
                verify_c = requests_php(int(verify_c), verify_c, 3)
            dict['camera'] = verify_c
            print(dict)
            with open('home_info.json','w+',encoding='utf-8')as make_file: #.json파일 쓰기형식, utf-8 로 인코딩해 오픈
                json.dump(dict, make_file, ensure_ascii=False, indent="\t")
            
        elif (i == '3') :
            print(i,"사용자의 가스제어 요청 감지")
            with open('home_info.json') as data_file:
                dict = json.load(data_file)
            choice_g = dict.get('gas')#로컬 저장값
            verify_g = php_read("gas.txt") #웹 저장값
            if(verify_g != choice_g): # 1 가스 잠김 수행, 2 카메라 회전 동작
                #가스 로컬 저장값과 가스 웹 저장값이 다를때 -> 사용자의 요청이 반영되지 않은상태 -> 동작수행
                print("사용자의 가스 제어 요청")
                verify_g = requests_php(int(verify_g), verify_g, 1)
            elif(verify_g == choice_g):
                verify_g = requests_php(int(verify_g), verify_g, 3)
            dict['gas'] = verify_g
            print(dict)
            with open('home_info.json','w+',encoding='utf-8')as make_file: #.json파일 쓰기형식, utf-8 로 인코딩해 오픈
                json.dump(dict, make_file, ensure_ascii=False, indent="\t")
#data = {'value': '1', 'choice' : '1', 'select' : '1'}
#value 2이면 가스일때 열림 1이면 잠김
#chocie 1이면 가스 2이면 카메라 회전 전달받은 값만큼 회전 메소드에 파리미터로 전달해 회전
#select 1이면 동작 필요 없음 2이면 동작필요(동작후 1로 자동 수정)
#main_2.py는 가스랑 카메라 회전 main_1.py 은 가스랑 사료
#requests.post('https://capstoneteamh.000webhostapp.com/ctrl.php', data=data)
main()
