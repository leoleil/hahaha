package cn.edu.bupt.ipoc.onps.model.entity;

import cn.edu.bupt.ipoc.onps.utils.LayerString;

import java.util.ArrayList;
import java.util.List;

public class SDHLink extends BasicLink {
    private boolean 		 	        inRing=false;       //链路是否是构成环
    private	String					    carriedType;	    //承载媒介，光缆还是WDM
    private	boolean			            gran=false;         //标示是否复用,false为没有小粒度业务，true为有小粒度业务
    private List<SDHRing>               SDHRing;            //该SDH链所属环
    private	List<BasicLink>	 	        basicLinkList;      //如果承载在光缆上的对应的Fiber层链路链表
    private	List<Timeslot>	 	        timeslotList;       //包含的时隙链
    private	List<Timeslot>	 	        exTimeslotList;     //扩容时隙

    public static class Builder extends BasicLink.Builder<Builder>{
        private boolean 		 	        inRing=false;       //链路是否是构成环
        private	String					    carriedType;	    //承载媒介，光缆还是WDM
        private	boolean			            gran=false;         //标示是否复用,false为没有小粒度业务，true为有小粒度业务
        private List<SDHRing>               SDHRing;            //该SDH链所属环
        private	List<BasicLink>	 	        basicLinkList;      //如果承载在光缆上的对应的Fiber层链路链表
        private	List<Timeslot>	 	        timeslotList;       //包含的时隙链
        private	List<Timeslot>	 	        exTimeslotList;     //扩容时隙
        private List<String> linkResIdList;
        /**
         * 节点间创建链路
         * 端口自动生成
         * @param id
         * @param name
         * @param fromNode
         * @param toNode
         */
        public Builder(String id, String name, CommonNode fromNode, CommonNode toNode) {
            super(id, name, LayerString.SDH, fromNode, toNode);
        }
        /**
         * 节点间创建链路
         * 端口自动生成
         * id自动生成
         * @param name
         * @param fromNode
         * @param toNode
         */
        public Builder(String name, CommonNode fromNode, CommonNode toNode) {
            super(name, LayerString.OTN, fromNode, toNode);
        }
        public Builder carriedType(String carriedType){
            this.carriedType = carriedType;
            return self();
        }

        public Builder layerRoute(List<BasicLink> basicLinkList){
            this.basicLinkList = basicLinkList;
            return self();
        }

        public Builder linkResIdList(List<String> linkResIdList){
            this.linkResIdList = linkResIdList;
            return self();
        }

        /**
         * 自动置入时隙
         * @param size
         * @return
         */
        @Override
        public Builder size(int size){
            if(timeslotList == null)
                timeslotList = new ArrayList<>();
            for(int i=0; i < size ; i++){
                Timeslot timeslot = new Timeslot();
                timeslotList.add(timeslot);
            }
            this.size = size;
            this.free = size;
            return self();
        }

        public Builder timeslot(List<Timeslot> timeslots){
            if(timeslots != null){
                this.timeslotList = timeslots;
                this.size = timeslots.size();
                this.free = timeslots.size();
            }else{
                this.timeslotList = new ArrayList<>();
            }
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BasicLink build() {
            return null;
        }
    }
    private SDHLink(Builder builder) {
        super(builder);
        this.carriedType = builder.carriedType;
        this.timeslotList = builder.timeslotList;
        this.basicLinkList = builder.basicLinkList;
        //占据层间路由
        if(carriedType.equals(LayerString.FIBER) && basicLinkList != null){
            for(BasicLink link:basicLinkList){
                if(link instanceof FiberLink){
                    ((FiberLink) link).occupyFiber(this);
                }
            }
        }else if(carriedType.equals(LayerString.WDM) && basicLinkList != null){
            for(BasicLink link:basicLinkList){
                if(link instanceof WDMLink){
                    ((WDMLink) link).occupyWavelength(this);
                }
            }
        }else if(carriedType.equals(LayerString.OTN) && basicLinkList != null){
            for(BasicLink link:basicLinkList){
                if(link instanceof OTNLink){
                    ((OTNLink) link).occupyOTU(this);
                }
            }
        }
        else if(carriedType.equals(LayerString.FIBER) && basicLinkList != null && builder.linkResIdList != null){
            for(int i=0;i<basicLinkList.size();i++){
                BasicLink link = basicLinkList.get(i);
                if(link instanceof FiberLink){
                    ((FiberLink) link).occupyFiber(this,builder.linkResIdList.get(i));
                }
            }
        }else if(carriedType.equals(LayerString.WDM) && basicLinkList != null && builder.linkResIdList != null){
            for(int i=0;i<basicLinkList.size();i++){
                BasicLink link = basicLinkList.get(i);
                if(link instanceof WDMLink){
                    ((WDMLink) link).occupyWavelength(this,builder.linkResIdList.get(i));
                }
            }
        } else if(carriedType.equals(LayerString.OTN) && basicLinkList != null && builder.linkResIdList != null){
            for(int i=0;i<basicLinkList.size();i++){
                BasicLink link = basicLinkList.get(i);
                if(link instanceof OTNLink){
                    ((OTNLink) link).occupyOTU(this,builder.linkResIdList.get(i));
                }
            }
        }
        else {
            basicLinkList = new ArrayList<>();
        }

    }
}