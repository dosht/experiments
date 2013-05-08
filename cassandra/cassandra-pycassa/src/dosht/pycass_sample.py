from pycassa.pool import ConnectionPool
from pycassa.columnfamily import ColumnFamily

pool = ConnectionPool("pykeyspace", ["localhost:9160"])
col_family = ColumnFamily(pool, "UserInfo")
col_family.insert("dosht2", {"email": "dosht2@dosht.com", "name": "mostafa"})
#print col_family.get("dosht2", columns=["email"])['email']
print col_family.get("dosht2")
b = col_family.batch()
b.insert("dodo", {"email": "dodo@fofo.com"})
b.remove("dosht2", ['name'])
b.send()
print col_family.get("dosht2")
print col_family.multiget(['dosht', 'dodo'])['dodo']

from pycassa.types import *


class User(object):
    key = UTF8Type()  # name key is mandatory
    email = AsciiType()
    age = IntegerType()

    def __repr__(self):
        return "User(key: %s, email: %s, age: %s)" % (self.key, self.email, self.age)

from pycassa.columnfamilymap import ColumnFamilyMap
cfmap = ColumnFamilyMap(User, pool, "UserInfo")
user = User()
user.key = "sasa"
user.email = "sasa@sasa.com"
user.age = 12
cfmap.insert(user)
print cfmap.get("sasa")
