package com.outsider.masterofpredictionbackend.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import com.influxdb.query.FluxRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class InfluxDBService {

    private final InfluxDBClient influxDBClient;

    @Value("${influxdb.bucket}")
    private String bucket;

    @Value("${influxdb.org}")
    private String org;

    public InfluxDBService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    // 데이터 생성 및 저장 메서드
    public void saveData(String measurement, String productId, double price) {
        Point point = Point.measurement(measurement)
                .addTag("product_id", productId)
                .addField("price", price)
                .time(Instant.now(), WritePrecision.MS);

        influxDBClient.getWriteApiBlocking().writePoint(bucket, org, point);
    }

    // 데이터 조회 메서드
    public List<Double> queryData(String productId, String measurement) {
        String flux = String.format("from(bucket: \"%s\") |> range(start: -1d) |> filter(fn: (r) => r._measurement == \"%s\" and r.product_id == \"%s\")", bucket, measurement, productId);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, org);

        List<Double> prices = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                prices.add((Double) record.getValueByKey("price"));
            }
        }

        return prices;
    }
}
