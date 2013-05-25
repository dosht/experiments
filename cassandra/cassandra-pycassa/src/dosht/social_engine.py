# Model
from pycassa.types import UTF8Type, AsciiType
from uuid import uuid1


class User(object):
    key = UTF8Type()  # username
    email = AsciiType()
    avatar = AsciiType()


# Startup
from pycassa.pool import ConnectionPool
from pycassa.columnfamily import ColumnFamily

pool = ConnectionPool("social_engine", ["localhost:9160"])

users = ColumnFamily(pool, "User")
posts = ColumnFamily(pool, "Post")
user_posts = ColumnFamily(pool, "UserPosts")
comments = ColumnFamily(pool, "Comments")
post_ratings = ColumnFamily(pool, "PostRatings")
user_ratings = ColumnFamily(pool, "UserRatings")
post_rating_counter = ColumnFamily(pool, "RatingsCounter")


def get_user(username):
    return users.get(username)


def get_post(post_key):
    return posts.get(post_key)


def get_user_posts(username):
    posts_keys = user_posts.get(username).keys()
    return posts.multiget(posts_keys)


def add_user(username, name, email, avatar=""):
    users.insert(username, {"name": name, "email": email, "avatar": avatar})
    return username


def add_post(username, content):
    uuid = str(uuid1())
    posts.insert(uuid, {"content": content, "username": username})
    user_posts.insert(username, {uuid: ""})
    return uuid


def get_comments(post_key):
    return comments.get(post_key)


def add_comment(post_key, username, comment):
    return comments.insert(post_key, {username: comment})


def get_user_rating(username, post_key):
    return user_ratings.get(username, [post_key])


def get_post_rating(post_key):
    return post_rating_counter.get(post_key)


def get_post_rating_detailed(post_key):
    return post_ratings.get(post_key)


def add_post_rating(post_key, username, rating):
    user_ratings.insert(username, {post_key: rating})
    post_ratings.insert(post_key, {username: rating})
    post_rating_counter.insert(post_key, {"1": rating, "1": 1})

# Add users and posts
#add_user("dosht", "mostafa", "dosht@hotmail.com")
#post_key = add_post("dosht", "hi alllllllll")
#print get_user("dosht")
#print get_post(post_key)
#print get_user_posts("dosht")

# Add comments
#post_key = get_user_posts("dosht").keys()[0]
#add_comment(post_key, "dosht", "Like +1")
#print get_comments(post_key)

# Rating
post_key = get_user_posts("dosht").keys()[0]
#add_post_rating("sdsd", "dosht", "4")
#print get_user_rating("dosht", post_key)
print get_post_rating(post_key)
#print get_post_rating_detailed(post_key)
