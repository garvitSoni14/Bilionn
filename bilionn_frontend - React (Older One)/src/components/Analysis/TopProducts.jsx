import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend } from "chart.js";
import { useEffect, useState } from "react";
import { Bar } from "react-chartjs-2";
import { latestOrder } from "../../Service/OrderService"; // ✅ import service

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

export default function TopProducts() {
  const [productData, setProductData] = useState(null);

  useEffect(() => {
    const fetchOrder = async () => {
      try {
        const response = await latestOrder();
        const orders = response.data;
        const productSales = {};

        orders.forEach((order) => {
          order.items.forEach((item) => {
            if (!productSales[item.name]) productSales[item.name] = 0;
            productSales[item.name] += item.quantity;
          });
        });

        const sortedProducts = Object.entries(productSales)
          .sort((a, b) => b[1] - a[1])
          .slice(0, 5);

        const labels = sortedProducts.map(([name]) => name);
        const values = sortedProducts.map(([, count]) => count);

        setProductData({
          labels,
          datasets: [
            {
              label: "Units sold",
              data: values,
              backgroundColor: "#0dcaf0",
            },
          ],
        });
      } catch (error) {
        console.error("Error fetching order:", error);
      }
    };

    fetchOrder();
  }, []);

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: "top" },
      title: { display: true, text: "Top 5 Sold Products" },
    },
  };

  return (
    <div>
      {productData ? (
        <Bar data={productData} options={options} />
      ) : (
        <p>Loading top products chart...</p>
      )}
    </div>
  );
}
