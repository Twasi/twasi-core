package net.twasi.core.webinterface.metrics;

import io.prometheus.client.Collector;
import io.prometheus.client.GaugeMetricFamily;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomMetrics {
    static final RAMCollector requests = new RAMCollector().register();

    static class RAMCollector extends Collector {
        public List<MetricFamilySamples> collect() {

            Runtime runtime = Runtime.getRuntime();
            double usedRam = (runtime.totalMemory() - runtime.freeMemory()) / 1024d / 1024d;
            double totalRam = runtime.totalMemory() / 1024d / 1024d;

            List<MetricFamilySamples> mfs = new ArrayList<>();
            GaugeMetricFamily ramGauge = new GaugeMetricFamily("ram", "Ram usage", Collections.singletonList("type"));
            ramGauge.addMetric(Collections.singletonList("ram_used"), usedRam);
            ramGauge.addMetric(Collections.singletonList("ram_total"), totalRam);

            mfs.add(ramGauge);
            return mfs;
        }
    }

}
