// 매개변수 : data (배열), tmp(중복 되는지 알고 싶은 number)
// 반환값 :중복이 되면 true, 중복되지 않으면 false
function isDuplicate(data, tmp) {
    console.log(data);
    let result = false;
    for (let i = 0; i < data.length; i++) {
        if (data[i] == tmp) {
            result = true;
            console.log(tmp, "중복");
            break;
        }
    }
    return result;
}

// 매개변수 : 값을 얻고자 하는 파라메터의 이름
// 반환괎 : 파라메터가 있다면 그 파라메터의 값을 반하고, 파라메터가 없다면 -1 반환
function getParameter(paramName){
    let returnVal = -1;
    let url = location.href;

    if (url.indexOf("?") != -1) { // 쿼리스트링이 있다면
        let queryStr = url.split('?')[1];
        let tmpArr = queryStr.split('&');
        for (let tmp of tmpArr) {
            if(tmp.split('=')[0] === paramName) { //s내가 찾는 파라메터라면///
                returnVal = tmp.split('=')[1];
                break;
            }
        }
    }

    return returnVal;
}


function saveCookie() {
    // 쿠키 저장 형식(문자열)
    // "쿠키이름=값;expires=쿠키만료일(날짜시간/string);[domain=도메인명;path=쿠키저장경로]"
    
    let now = new Date();
    now.setDate(now.getDate() + 7); // 현재날짜 + 7 (7일 이후에 만료되도록...)

    let myCookie = "";
    myCookie = "myCook=dongas;expires=" + now.toUTCString();
    console.log(myCookie);

    document.cookie = myCookie; // 쿠키 저장
}
