import { Injectable } from "@angular/core";
import { ChartReadyService } from "./chart-ready.service";

@Injectable({ providedIn: "root" })
export class ApexInterceptorService {

	private initialized = false;

	constructor(private chartReady: ChartReadyService) {
		this.initInterceptor();
	}

	private initInterceptor() {
		if (this.initialized) return;

		const Apex = (window as any).ApexCharts;
		if (!Apex || !Apex.prototype) {
			setTimeout(() => this.initInterceptor(), 50);
			return;
		}

		const origRender = Apex.prototype.render;

		const self = this;

		Apex.prototype.render = async function () {

			// Регистрируем график ровно 1 раз
			if (!this.__registered) {
				this.__registered = true;
				self.chartReady.registerChart();
			}

			// Принудительно вставляем mounted event
			this.updateOptions({
				chart: {
					events: {
						mounted: () => {
							// mounted вызывается 1 раз — идеально
							self.chartReady.chartRendered();
						}
					}
				}
			}, false, true);

			return await origRender.apply(this, arguments);
		};

		this.initialized = true;
	}
}
