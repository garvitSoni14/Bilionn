import { Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
 } from "chart.js";
import {Bar} from "react-chartjs-2";
import { useEffect, useState } from "react";    
import { latestOrder } from "../../Service/OrderService";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);
export default function SalesByDate() {
    const[chartData, setChartData] = useState(null);
    useEffect(() => {
        const fetchOrders = async () => {
            try{
                const response = await latestOrder();
                const orderData = response.data;
                const salesByDate={};
                orderData.forEach((order)=>{
                    const date = new Date(order.createdAt).toLocaleDateString("en-US");
                    if(!salesByDate[date]) salesByDate[date]=0;
                    salesByDate[date] += order.grandTotal;
                });
                const labels = Object.keys(salesByDate);
                const values = Object.values(salesByDate);
                setChartData({
                    labels,
                    datasets:[
                        {
                            label: "Total sales (₹)",
                            data: values,
                            backgroundColor: "#20c997",
                        }
                    ]
                })
            }catch(error){
                console.error("Error fetching order: ",error);
            }
        }
        fetchOrders();
    }, []);

    const options = {
        responsive:true,
        maintainAspectRatio: false,
        plugins: {
            legend: {position: "top"},
            title: {display: true, text: "Daily Sales Report"},
        }
    };
        
    

    // ------------------------dummmydata

    // const data = {
    //     labels: ["Product A", "Product B", "Product C"],
    //     datasets: [
    //         {
    //             label: "Sales",
    //             data:[120, 90, 150],
    //             backgroundColor: "#20c997",
    //         }
    //     ]
    // };
    // const options = {
    //     responsive: true,
    //     plugins: {
    //         legent: {position: "top"},
    //         title: {display: true, text: "Sample bar chart"},
    //     }
    // };

    return (
        <div>
            {/* <Bar data = {data} options={options}/> */}
            {chartData? <Bar data = {chartData} options={options}/>: <p>Loading chart....</p>}
        </div>
    );

}