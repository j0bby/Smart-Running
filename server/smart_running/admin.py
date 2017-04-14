from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from django.contrib.auth.models import User

from smart_running.models import UserProfile, Route, Marker, RouteRating

admin.site.unregister(User)


class UserProfileInline(admin.StackedInline):
    model = UserProfile


class UserProfileAdmin(UserAdmin):
    inlines = [UserProfileInline, ]


class MarkerInline(admin.TabularInline):
    model = Marker.routes.through
    verbose_name = "Marker"
    verbose_name_plural = "Markers"


class RouteAdmin(admin.ModelAdmin):
    inlines = [MarkerInline, ]
    readonly_fields = ('id',)


class MarkerAdmin(admin.ModelAdmin):
    readonly_fields = ('routes', 'id')


class RatingAdmin(admin.ModelAdmin):
    pass


admin.site.register(User, UserProfileAdmin)
admin.site.register(Route, RouteAdmin)
admin.site.register(Marker, MarkerAdmin)
admin.site.register(RouteRating, RatingAdmin)
