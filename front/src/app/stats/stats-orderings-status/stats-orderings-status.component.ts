import {Component, Input, OnInit} from '@angular/core';
import {StatsService} from "../stats.service";
import {ChartComponent} from "ng-apexcharts";

@Component({
	selector: 'app-stats-orderings-status',
	imports: [
		ChartComponent
	],
	templateUrl: './stats-orderings-status.component.html',
	standalone: true,
})

export class StatsOrderingsStatusComponent implements OnInit {

	colors: string[] = [
		"#8A4BD9",
		"#D9230F",
		"#FFD700",
		"#8CD93F",
		"#0286B4",
	];

	// @Input() colors: any[] = [];

	chartOptions: any;

	names: any[] = [];
	values: any[] = [];

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.orderingsStatus().subscribe({
			next: (res: any) => {
				this.names = res.data.names;
				this.values = res.data.values;
				this.draw();
			},
			error: (e: any) => console.log(e.error)
		})
	}

	draw() {
		this.chartOptions = {
			series: [
				{
					name: "Количество",
					data: this.values
				}
			],
			chart: {
				height: 400,
				type: "bar",
			},
			colors: this.colors,
			plotOptions: {
				bar: {
					columnWidth: "45%",
					distributed: true
				}
			},
			dataLabels: {
				enabled: false
			},
			legend: {
				show: false
			},
			grid: {
				show: false
			},
			xaxis: {
				categories: this.names,
				labels: {
					style: {
						colors: this.colors,
						fontSize: "12px"
					}
				}
			}
		};
	}

}
