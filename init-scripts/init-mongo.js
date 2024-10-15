db = connect("localhost:27017/admin");

// 관리자 권한으로 인증
db.auth("ohgiraffers", "ohgiraffers");

// forecasthub 데이터베이스를 생성하고 사용할 사용자 지정
db = db.getSiblingDB("forecasthub");

// 필요한 컬렉션을 생성하거나 초기 데이터를 삽입 (필요에 따라 생략 가능)
// db.createCollection("forecast_data");

// // 예시 데이터 삽입 (필요에 따라 생략 가능)
// db.forecast_data.insert({
//     city: "Seoul",
//     temperature: 22,
//     condition: "Sunny"
// });

print("Database and collection created, initial data inserted.");


// 초기 데이터 삽입 (필요한 경우)
// db.mycollection.insertOne({ key: "value" });
