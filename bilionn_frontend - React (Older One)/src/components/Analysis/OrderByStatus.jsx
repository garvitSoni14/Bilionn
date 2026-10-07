import {Chart as ChartJS, CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement } from "chart.js";
import { useEffect, useState } from "react";
import { Doughnut } from "react-chartjs-2";
import { latestOrder } from "../../Service/OrderService"; // ✅ import service

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend, ArcElement);

export default function OrderByStatus() {
    const [statusData, setStatusData] = useState(null); 

    useEffect(() => {
        const fetchOrder = async () => {
            try{
            const response = await latestOrder();
            const orders = response.data;
            const orderStatus = {};
            orders.forEach((order) => {
                const status = order.paymentDetails?.status || "UNKNOWN";
                if (!orderStatus[status]) orderStatus[status] = 0;
                orderStatus[status] += 1;
            });

            setStatusData({
                labels:Object.keys(orderStatus),
                datasets: [
                    {
                        label:"Number or Orders",
                        data: Object.values(orderStatus),
                        backgroundColor: ["#20c997", "#ffc107", "#dc3545", "#0d6efd"]
                    }
                ]
            });
        }catch(error){
            console.error("Error fetching order: ", error);
        }
    };
        fetchOrder();
    },[]);

    return (
        <div>
            {statusData? (<Doughnut data={statusData} options={{responsive:true, maintainAspectRatio: false,plugins: {legend: {position: "top"},title: {display:true, text: "Orders by Status"}}}}/>
            ):(<p>Loading order status chart...</p>

            )}
        </div>
    );
}