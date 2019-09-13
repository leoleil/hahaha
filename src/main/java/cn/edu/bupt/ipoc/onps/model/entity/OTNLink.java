package cn.edu.bupt.ipoc.onps.model.entity;

import cn.edu.bupt.ipoc.onps.utils.LayerString;
import cn.edu.bupt.ipoc.onps.utils.LinkStatusString;

import java.util.ArrayList;
import java.util.List;

public class OTNLink extends BasicLink{
    private	String carriedType;//承载媒介，光缆还是WDM
    private List<BasicLink> basicLinkList;//层间路由
    private	List<OTU> OTUList;//包含的ONU链
    private	List<OTU> exOTUList;//扩容ONU链路
    public static class Builder extends BasicLink.Builder<Builder>{
        private	String carriedType;//承载媒介，光缆还是WDM
        private List<BasicLink> basicLinkList;//层间路由
        private	List<OTU> OTUList;//包含的ONU链
        private	List<OTU> exOTUList;//扩容ONU链路
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
            super(id, name, LayerString.OTN, fromNode, toNode);
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
         * 自动置入ONU
         * @param size
         * @return
         */
        @Override
        public Builder size(int size){
            if(OTUList == null)
                OTUList = new ArrayList<>();
            for(int i=0; i < size ; i++){
                OTU OTU = new OTU();
                OTUList.add(OTU);
            }
            this.size = size;
            this.free = size;
            return self();
        }

        public Builder onuList(List<OTU> OTUList){
            if(OTUList != null){
                this.OTUList = OTUList;
                this.size = OTUList.size();
                this.free = size;
            }
            else
                this.OTUList = new ArrayList<>();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public BasicLink build() {
            return new OTNLink(this);
        }
    }
    private OTNLink(Builder builder) {
        super(builder);
        //占据层间路由
        this.carriedType = builder.carriedType;
        this.OTUList = builder.OTUList;
        this.basicLinkList = builder.basicLinkList;
        if(carriedType.equals(LayerString.FIBER) && basicLinkList != null && builder.linkResIdList == null){
            for(BasicLink link:basicLinkList){
                if(link instanceof FiberLink){
                    ((FiberLink) link).occupyFiber(this);
                }
            }
        }else if(carriedType.equals(LayerString.WDM) && basicLinkList != null && builder.linkResIdList == null){
            for(BasicLink link:basicLinkList){
                if(link instanceof WDMLink){
                    ((WDMLink) link).occupyWavelength(this);
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
        }
        else{
            basicLinkList = new ArrayList<>();
        }
    }

    public boolean occupyOTU(BasicLink link){
        if(free > 0){
            for(OTU OTU : OTUList){
                if(OTU.getStatus().equals(LinkStatusString.FREE)){
                    OTU.addCarryLink(link);
                    this.free --;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean occupyOTU(BasicLink link,String otuID){
        if(free > 0){
            for(OTU OTU : OTUList){
                if(OTU.getStatus().equals(LinkStatusString.FREE) && OTU.getId().equals(otuID)){
                    OTU.addCarryLink(link);
                    this.free --;
                    return true;
                }
            }
        }
        return false;
    }

}