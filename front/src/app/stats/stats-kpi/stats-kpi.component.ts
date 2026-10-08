import { Component, OnInit } from '@angular/core';
import { StatsService } from '../stats.service';
import { forkJoin } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
	selector: 'app-stats-kpi',
	standalone: true,
	imports: [CommonModule],
	templateUrl: './stats-kpi.component.html'
})
export class StatsKpiComponent implements OnInit {

	booksTotal = 0;
	booksTotalInLibrary = 0;
	avgScore = 0;
	monthOrders = 0;
	monthIncome = 0;

	loading = true;
	errorMessage: string | null = null;

	constructor(private statsService: StatsService) {}

	ngOnInit() {
		this.loadKpi();
	}

	private loadKpi() {
		this.loading = true;
		this.errorMessage = null;

		forkJoin({
			booksTotal: this.statsService.kpiBooksTotal(),
			booksTotalInLibrary: this.statsService.kpiBooksTotalInLibrary(),
			avgScore: this.statsService.kpiBooksAvgScore(),
			monthOrders: this.statsService.kpiOrderingsMonthly(),
			monthIncome: this.statsService.kpiIncomeMonthly()
		}).subscribe({
			next: res => {
				this.booksTotal = res.booksTotal.data;
				this.booksTotalInLibrary = res.booksTotalInLibrary.data;
				this.avgScore = res.avgScore.data;
				this.monthOrders = res.monthOrders.data;
				this.monthIncome = res.monthIncome.data;
				this.loading = false;
			},
			error: err => {
				console.error('Ошибка загрузки KPI:', err);
				this.errorMessage = 'Не удалось загрузить KPI';
				this.loading = false;
			}
		});
	}

	// --- Цвета карточек ---
	getBooksBg(value: number) {
		if (value < 50) return { 'background-color': '#ff8080' };
		if (value < 100) return { 'background-color': '#b399ff' };
		if (value < 200) return { 'background-color': '#FFD700' };
		return { 'background-color': '#8CD93F' };
	}

	getAvgScoreBg(value: number) {
		if (value < 4) return { 'background-color': '#ff8080' };
		if (value < 6) return { 'background-color': '#b399ff' };
		if (value < 8) return { 'background-color': '#FFD700' };
		return { 'background-color': '#8CD93F' };
	}

	getOrdersBg(value: number) {
		if (value < 15) return { 'background-color': '#ff8080' };
		if (value < 30) return { 'background-color': '#b399ff' };
		if (value < 60) return { 'background-color': '#FFD700' };
		return { 'background-color': '#8CD93F' };
	}

	getIncomeBg(value: number) {
		if (value < 200) return { 'background-color': '#ff8080' };
		if (value < 400) return { 'background-color': '#b399ff' };
		if (value < 600) return { 'background-color': '#FFD700' };
		return { 'background-color': '#8CD93F' };
	}
}
