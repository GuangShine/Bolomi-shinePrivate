--
--    BOLOMI  -- M2M  -http://www.bolomi365.com --http://www.cloudinnov.com/
--    Copyright © 2014.中科云创（北京）科技有限公司 All rights reserved.
--
--

-- NOTE: this script is probably not maintained and thus should be used as a guide only. Do not delete data based
-- upon the unvalidated results of these statements. Also, the absence of results from these statements does not
-- necessarily mean that no problems exist.

-- Orphaned user comments
select c.* 
from userComments c
  left join events e on c.typeKey=e.id
  left join dataPoints p on c.typeKey=p.id
where (c.commentType=1 and e.id is null)
  or (c.commentType=2 and p.id is null)

-- Orphaned mailing list members
select m.*
from mailingListMembers m
  left join users u on m.typeId=u.id
where (m.typeId=2 and u.id is null)
  
-- Orphaned point value annotations
select a.*
from pointValueAnnotations a
  left join users u on a.sourceId=u.id
  left join eventHandlers h on a.sourceId=h.id
where (a.sourceId=1 and u.id is null)
  or (a.sourceId=2 and h.id is null)
  
select e.* -- The question is, of course, should the events really be deleted? They get purged eventually anyway.
from events e
  left join dataPoints p on e.typeRef1=p.id
  left join pointEventDetectors ped on e.typeRef2=ped.id
  left join dataSources d on e.typeRef1=d.id
  left join compoundEventDetectors ced on e.typeRef1=ced.id
  left join scheduledEvents s on e.typeRef1=s.id
  left join publishers pb on e.typeRef1=pb.id
where (e.typeId = 1 and p.id is null)
  or (e.typeId = 1 and ped.id is null)
  or (e.typeId = 3 and d.id is null)
  or (e.typeId = 5 and ced.id is null)
  or (e.typeId = 6 and s.id is null)
  or (e.typeId = 7 and pb.id is null)

-- Orphaned event handlers
select e.*
from eventHandlers e
  left join dataPoints p on e.eventTypeRef1=p.id
  left join pointEventDetectors ped on e.eventTypeRef2=ped.id
  left join dataSources d on e.eventTypeRef1=d.id
  left join compoundEventDetectors ced on e.eventTypeRef1=ced.id
  left join scheduledEvents s on e.eventTypeRef1=s.id
  left join publishers pb on e.eventTypeRef1=pb.id
where (e.eventTypeId = 1 and p.id is null)
  or (e.eventTypeId = 1 and ped.id is null)
  or (e.eventTypeId = 3 and d.id is null)
  or (e.eventTypeId = 5 and ced.id is null)
  or (e.eventTypeId = 6 and s.id is null)
  or (e.eventTypeId = 7 and pb.id is null)
