#概要
  有限元领域、网格划分及节点值分布数据的图形显示软件。  
  
  ![](https://github.com/kong0102/FEM-2DViewer/blob/master/screenshots1.png)   
  
                    显示设计领域   
    
    
  ![](https://github.com/kong0102/FEM-2DViewer/blob/master/screenshots2.png)   
  
                    显示单元网格   
    
    
  ![](https://github.com/kong0102/FEM-2DViewer/blob/master/screenshots3.png)   
  
                    节点值大小分布   
    
    
  ![](https://github.com/kong0102/FEM-2DViewer/blob/master/screenshots4.png)   
  
                   应力大小分布及最大最小值   
    
    
#操作方式
  将.el2及.nv2文件拖放至界面中打开。
  
#数据文件说明
  .el2 为单元网格及节点的编号和坐标数据文件，其格式：   
  
  节点数    
  
  节点1的X坐标 Y坐标   
  
  节点2的X坐标 Y坐标   
  
  ......   
  
  单元数  
  
  单元材料类型 单元节点1的编号 单元节点2的编号 单元节点3的编号 单元节点4的编号<br/>  
  （没有节点4可为0）  
  
  ......  
  
   
   
  .nv2 为对应节点值数据文件，其格式：  
  
  节点数  
  
  节点1节点值  
  
  节点2的节点值  
  
  ......  
  

  数据文件格式可参考示例文件 sample.el2,sample.nv2
  
#设置文件说明
  设置文件为 settings.txt , 若没有将自动创建。其格式：  
  
  elementColor可设置最多3种不同材料领域颜色  
  
  edgeColor为边界颜色  
  
  backgroundColor为界面背景颜色  
  
  zoom为图形缩放比例  
  
  nodalValueScaleMax为显示的节点值最大值  
  
  nodalValueScaleMin为显示的节点值最小值  
  
