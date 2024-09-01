// 선택한 데이터베이스로 이동
db = db.getSiblingDB('forecasthub');

// 사용자 생성
db.createUser({
    user: "myuser",
    pwd: "mypassword",
    roles: [{ role: "readWrite", db: "mydatabase" }]
});

// 초기 데이터 삽입 (필요한 경우)
// db.mycollection.insertOne({ key: "value" });
