import { Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    ArcElement
} from "chart.js";
import { useEffect, useState } from "react";
import {Chart, Pie} from "react-chartjs-2";
import { latestOrder } from "../../Service/OrderService";

ChartJS.register(Title, Tooltip, Legend, ArcElement);

export default function SalesByPayment() {
    const [paymentData, setPaymentData] = useState(null);   

    useEffect(() => {
        const fetchOrder = async () => {
            try{
                const response = await latestOrder();
                const orderData = response.data;
                const salesByPayment = {};
                orderData.forEach((order) => {
                    const method = order.paymentMethod || "Unknown";
                    if(!salesByPayment[method]) salesByPayment[method] = 0;
                    salesByPayment[method] += order.grandTotal;
                });
                setPaymentData({
                    labels: Object.keys(salesByPayment),
                    datasets:[
                        {
                            label: "Total sales (₹)",
                            data: Object.values(salesByPayment),
                            backgroundColor: ["#20c997", "#0d6efd", "#ffc107", "#dc3545"]
                        }
                    ]
                })

            }catch(error){
            console.error("Error fetching order: ",error);
            }
        }
        fetchOrder();
    }, []);

    return (
        <div>
            {paymentData? (
                <div>
                    <Pie data = {paymentData} options = {{responsive: true, maintainAspectRatio:false, plugins:{legend:{position: "top", labels:{ padding: 20}},title:{display:true, text:"Sales by payment method", font: {size:14}}}}}/>
                </div>
            ):(
                <div>Loading payment method chart...</div>
            )}
        </div>
    )
}