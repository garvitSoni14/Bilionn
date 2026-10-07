import {Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend} from "chart.js";
import {Line} from "react-chartjs-2";
import { useEffect, useState } from "react";
import { latestOrder } from "../../Service/OrderService"; // ✅ import service

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);    

export default function MonthlySales() {
    const [monthlyData, setMonthlyData] = useState(null);

    useEffect(()=>{
        const fetchOrder = async () => {
            try{
                const response = await latestOrder();
                const orderData = response.data;
                const salesByMonth = {};

                orderData.forEach((order)=>{
                    const date = new Date(order.createdAt);
                    const monthYear = `${date.toLocaleString("default",{month:"short"})} ${date.getFullYear()}`;
                    if(!salesByMonth[monthYear]) salesByMonth[monthYear]=0;
                    salesByMonth[monthYear] += order.grandTotal;
                });

                const labels = Object.keys(salesByMonth);
                const values = Object.values(salesByMonth);
                setMonthlyData({
                    labels,
                    datasets: [
                        {
                            label: "Monthly Sales (₹)",
                            data: values,
                            borderColor: "#0d6efd",
                            backgroundColor: "rgba(13, 110, 253, 0.2)",
                            fill: true,
                            tension: 0.3
                        }
                    ]
                });
            }catch(error){
                console.error("Error fetching orders: ", error);
            }
        };
        fetchOrder();
            }, []);

    return (
        <div>
            {monthlyData ? (
                <Line data={monthlyData} options ={{ responsive: true, maintainAspectRatio: false, plugins:{legend:{position: "top"}, title:{display: true, text:"Monthly Sales Trend"}}}}/>
            ):(
                <p>Loading monthly sales trend chart...</p>
            )}
        </div>
    );
}