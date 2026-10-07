import "./Analysis.css";
import MonthlySales from "./MonthlySales";
import OrderByStatus from "./OrderByStatus";
import SalesByDate from "./SalesByDate";
import SalesByPayment from "./SalesByPayment";
import TopProducts from "./TopProducts";

const Analysis = () => {
  return (
    <div className="analysis-container">
      <h3 className="analysis-header">📊 Sales Analysis</h3>

      <div className="analysis-grid">
        {/* Big trend chart */}
        <div className="analysis-card sales-date-card">
          <SalesByDate />
        </div>

        {/* Payment method chart */}
        <div className="analysis-card payment-card">
          <SalesByPayment />
        </div>

        {/* Top products */}
        <div className="analysis-card products-card">
          <TopProducts />
        </div>

        {/* Order status chart */}
        <div className="analysis-card status-card">
          <OrderByStatus />
        </div>

        {/* Monthly sales wide chart */}
        <div className="analysis-card monthly-card">
          <MonthlySales />
        </div>
      </div>
    </div>
  );
};

export default Analysis;
