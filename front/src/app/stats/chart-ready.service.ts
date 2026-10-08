import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ChartReadyService {
	private totalCharts = 0;
	private renderedCharts = 0;
	private resolver: (() => void) | null = null;

	waitForAllCharts(): Promise<void> {
		return new Promise(resolve => {
			this.resolver = resolve;
			this.checkDone();
		});
	}

	registerChart() {
		this.totalCharts++;
	}

	chartRendered() {
		this.renderedCharts++;
		this.checkDone();
	}

	private checkDone() {
		if (this.totalCharts > 0 && this.renderedCharts >= this.totalCharts) {
			this.resolver?.();
		}
	}
}
