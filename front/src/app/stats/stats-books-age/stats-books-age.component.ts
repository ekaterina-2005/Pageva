import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { StatsService } from '../stats.service';
import { ChartComponent } from 'ng-apexcharts';

@Component({
	selector: 'app-stats-books-age',
	standalone: true,
	imports: [ChartComponent],
	templateUrl: './stats-books-age.component.html'
})
export class StatsBooksAgeComponent implements OnInit, OnChanges {

	@Input() age: string = '';
	@Input() categoryId: string = '';

	chartOptions: any;

	names: string[] = [];
	values: number[] = [];

	constructor(private statsService: StatsService) {}

	ngOnInit(): void {
		this.loadData();
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['age'] || changes['categoryId']) {
			this.loadData();
		}
	}

	private loadData(): void {
		this.statsService.booksAge(this.age, this.categoryId).subscribe({
			next: (res: any) => {
				if (res && res.data) {
					this.names = res.data.names || [];
					this.values = res.data.values || [];
				} else {
					this.names = [];
					this.values = [];
				}

				// приводим все значения к числам
				for (let i = 0; i < this.values.length; i++) {
					this.values[i] = Number(this.values[i]) || 0;
				}

				this.draw();
			},
			error: (e: any) => console.log(e.error)
		});
	}

	private draw(): void {
		const total = this.values.reduce((a, b) => a + b, 0);

		this.chartOptions = {
			labels: this.names,
			series: this.values,
			chart: {
				type: 'donut',
				height: 400
			},
			dataLabels: {
				enabled: true,
				style: {
					fontSize: '16px',
					fontWeight: 500,
					colors: ['#fff']
				},
				formatter: (_: number, opts: any) => {
					const value = this.values[opts.seriesIndex];
					const percent = total ? (value / total) * 100 : 0;
					return percent.toFixed(1) + '%';
				}
			},
			tooltip: {
				y: {
					formatter: (value: number) => {
						const percent = total ? (value / total) * 100 : 0;
						return percent.toFixed(1) + '%';
					}
				}
			},
			colors: ['#3D5AFE', '#0286B4', '#00C853', '#8CD93F'],
			fill: {
				type: 'gradient'
			},
			responsive: [
				{
					breakpoint: 480,
					options: {
						chart: { width: 200 },
						legend: { position: 'bottom' }
					}
				}
			]
		};
	}
}
