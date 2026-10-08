import {Routes} from '@angular/router';
import {ErrorComponent} from "./error/error.component";
import {StatsComponent} from "./stats/stats.component";
import {UserComponent} from "./user/user.component";
import {LoginComponent} from "./auth/login/login.component";
import {RegComponent} from "./auth/reg/reg.component";
import {MainComponent} from "./main/main.component";
import {CategoryComponent} from "./category/category.component";
import {DiscountComponent} from "./discount/discount.component";
import {BookComponent} from "./book/book.component";
import {BookPageComponent} from "./book/book-page/book-page.component";
import {BookAddComponent} from "./book/book-add/book-add.component";
import {BookUpdateComponent} from "./book/book-update/book-update.component";
import {OrderingComponent} from "./ordering/ordering.component";
import {AccountComponent} from "./account/account.component";
import {SubscriptionComponent} from "./subscription/subscription.component";
import {SubscriptionAddComponent} from "./subscription/subscription-add/subscription-add.component";
import {SubscriptionUpdateComponent} from "./subscription/subscription-update/subscription-update.component";
import {SubscriptionPageComponent} from "./subscription/subscription-page/subscription-page.component";
import {SubscriptionOrderingComponent} from "./subscription-ordering/subscription-ordering.component";

export const routes: Routes = [

	{path: "", component: MainComponent},

	{path: "reg", component: RegComponent},
	{path: "login", component: LoginComponent},

	{path: "account", component: AccountComponent},

	{path: "users", component: UserComponent},

	{path: "categories", component: CategoryComponent},

	{path: "discounts", component: DiscountComponent},

	{path: "books", component: BookComponent},
	{path: "book", component: BookPageComponent},
	{path: "book_add", component: BookAddComponent},
	{path: "book_update", component: BookUpdateComponent},

	{path: "subscriptions", component: SubscriptionComponent},
	{path: "subscription", component: SubscriptionPageComponent},
	{path: "subscription_add", component: SubscriptionAddComponent},
	{path: "subscription_update", component: SubscriptionUpdateComponent},

	{path: "subscriptions_orderings", component: SubscriptionOrderingComponent},

	{path: "orderings", component: OrderingComponent},

	{path: "stats", component: StatsComponent},

	{path: "error", component: ErrorComponent},
	{path: "**", component: ErrorComponent},

];
